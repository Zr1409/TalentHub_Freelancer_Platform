import { Link, useNavigate, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import FadeInWhenVisible from "@/components/animations/FadeInWhenVisible";
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import {
  Clock,
  Building,
  CircleDollarSign,
  Hourglass,
  User,
  Flag,
  FileText,
  CheckCircle,
  Loader2,
  Plus,
  Eye,
  UserCheck,
  Calendar,
  MapPin,

} from "lucide-react";
import api from "@/api/axiosConfig";
import cvService, { CV } from "@/api/cvService";
import { notification } from "antd";
import { formatCurrency } from "@/lib/utils";
import ReportDialog from "@/components/report/ReportDialog";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import LoadingEffect from "@/components/ui/LoadingEffect";

interface JobDetailResponse {
  id: number;
  title: string;
  companyName?: string;
  companyImage?: string;
  companyAddress?: string;
  clientId: number;
  firstName: string;
  lastName: string;
  location?: string;
  type: string;
  fromPrice: number;
  toPrice: number;
  hourWork: number;
  description: string;
  skillNames: string[];
  experience?: string;
  deadline?: string;
  totalApplicants?: number;
  totalViews: number;
  duration?: number;
  scope?: string;
  jobOpportunity?: boolean;
  status?: string;
  createdAt: string;
  createdTimeFormatted: string;
  endDate: string;
  remainingTimeInHours: number;
  remainingTimeFormatted: string;
}

interface JobFreelancerInfo {
  id: number;
  status: string;
  jobId: number;
  freelancerId: number;
  saved: boolean;
}
interface Job {
  id: number;
  title: string;
  companyName: string;
  hourWork: number;
  fromPrice: number;
  toPrice: number;
  description: string;
  skillName: string[];
  categoryName: string;
  applied?: boolean;
  seen?: boolean;
  remainingTimeFormatted?: string;
  createdTimeFormatted?: string;
}

const JobDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [job, setJob] = useState<JobDetailResponse | null>(null);
  const [isClient, setClient] = useState(null);
  const [jobFreelancerInfo, setJobFreelancerInfo] =
    useState<JobFreelancerInfo | null>(null);
  const [cvs, setCVs] = useState<CV[]>([]);
  const [cvPreviews, setCvPreviews] = useState<{ [key: number]: string }>({});
  const [isApplyDialogOpen, setIsApplyDialogOpen] = useState(false);
  const [selectedCvId, setSelectedCvId] = useState<number | null>(null);
  const [uploading, setUploading] = useState(false);
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState<any>(null);
  const [loadingRecommendedJobs, setLoadingRecommendedJobs] = useState(true);
  const [recommendedJobs, setRecommendedJobs] = useState<Job[]>([]);
  useEffect(() => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, [id]);
  useEffect(() => {
    const fetchRecommendedJobs = async () => {
      if (userInfo && userInfo.freelancerId) {
        try {
          setLoadingRecommendedJobs(true);
          const response = await api.get(
            `/v1/jobs/recommended/${userInfo.freelancerId}`
          );
          if (response.status === 200) {
            setRecommendedJobs(response.data);
          }
          setLoadingRecommendedJobs(false);
        } catch (error) {
          console.error("Error fetching recommended jobs:", error);
          setLoadingRecommendedJobs(false);
        }
      } else {
        setLoadingRecommendedJobs(false);
      }
    };

    fetchRecommendedJobs();
  }, [userInfo]);
  useEffect(() => {
    const userInfoStr = localStorage.getItem("userInfo");
    if (userInfoStr) {
      try {
        const parsedUserInfo = JSON.parse(userInfoStr);
        setUserInfo(parsedUserInfo);
      } catch (error) {
        console.error("Error parsing user info:", error);
      }
    }
  }, []);
  const freelancerId = JSON.parse(
    localStorage.getItem("userInfo") || "{}"
  ).freelancerId;

  const fetchJobDetail = async () => {
    try {
      const response = await api.get(`/v1/jobs/detail-job/${id}`);
      setJob(response.data);
    } catch (error) {
      console.error("Error fetching job details:", error);
    }
  };
  useEffect(() => {
    fetchJobDetail();
  }, [id]);

  useEffect(() => {
    const data = JSON.parse(localStorage.getItem("userInfo") || "{}");
    if (data.clientId) {
      setClient(data.clientId);
    } else {
      if (!data?.freelancerId) {
        navigate("/login");
      }

      const handleViewJob = async () => {
        const response = await api.post("/v1/jobs/view", {
          freelancerId: data?.freelancerId,
          jobId: Number(id),
        });

        setJobFreelancerInfo(response?.data || null);
        await fetchJobDetail();
      };

      handleViewJob();
    }
  }, []);

  const fetchCVs = async () => {
    try {
      const response = await cvService.getCVsByFreelancerId(freelancerId);
      if (response.data) {
        setCVs(response.data);

        const previewsObj: { [key: number]: string } = {};
        for (const cv of response.data) {
          try {
            const previewUrl = await cvService.previewCV(cv.url);
            previewsObj[cv.id] = previewUrl;
          } catch (error) {
            console.error(`Error creating preview for CV ${cv.id}:`, error);
          }
        }
        setCvPreviews(previewsObj);
      }
    } catch (error) {
      console.error("Error loading CV list:", error);
      notification.error({
        message: "Lỗi",
        description: "Không thể tải danh sách CV. Vui lòng thử lại sau.",
      });
    }
  };

  const handleOpenApplyDialog = () => {
    fetchCVs();
    setIsApplyDialogOpen(true);
  };

  const handleUploadCV = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (file.type !== "application/pdf") {
      notification.error({
        message: "Lỗi",
        description: "Chỉ cho phép upload file PDF",
      });
      return;
    }

    try {
      setUploading(true);
      await cvService.uploadCV(file, freelancerId);
      notification.success({
        message: "Thành công",
        description: "Upload CV thành công",
      });
      await fetchCVs();
    } catch (error) {
      console.error("Error uploading CV:", error);
      notification.error({
        message: "Lỗi",
        description: "Không thể upload CV. Vui lòng thử lại sau.",
      });
    } finally {
      setUploading(false);
      event.target.value = "";
    }
  };

  const handleApplyJob = async () => {
    if (!selectedCvId) {
      notification.error({
        message: "Lỗi",
        description: "Vui lòng chọn CV",
      });
      return;
    }

    try {
      const response = await api.post("/v1/jobs/apply", {
        jobId: Number(id),
        freelancerId: freelancerId,
        cvId: selectedCvId,
      });

      if (response.status !== 200) {
        notification.error({
          message: "Lỗi dữ liệu",
          description: response.data.message || "Dữ liệu không hợp lệ",
        });
        return;
      }

      notification.info({
        message: "Thông báo",
        description: "Ứng tuyển thành công. Vui lòng chờ để được chấp nhận",
      });

      setJobFreelancerInfo(response?.data || null);
      await fetchJobDetail();
      setIsApplyDialogOpen(false);
    } catch (error) {
      console.error("Error applying for job:", error);
      notification.error({
        message: "Lỗi",
        description: "Không thể ứng tuyển. Vui lòng thử lại sau.",
      });
    }
  };

  const handleSaveJob = async () => {
    const data = JSON.parse(localStorage.getItem("userInfo") || "{}");

    if (!data?.freelancerId) {
      navigate("/login");
    }
    const response = await api.post("/v1/jobs/save", {
      freelancerId: data?.freelancerId,
      jobId: Number(id),
    });
    if (response.status !== 200) {
      notification.error({
        message: "Lỗi dữ liệu",
        description: response.message || "Dữ liệu không hợp lệ",
      });
    }
    notification.info({
      message: "Thông báo",
      description: "Lưu việc thành công",
    });
    setJobFreelancerInfo(response?.data || null);
  };

  const handleUnSaveJob = async () => {
    const response = await api.post("/v1/jobs/unsave", {
      freelancerId: jobFreelancerInfo?.freelancerId,
      jobId: Number(id),
    });
    if (response.status !== 200) {
      notification.error({
        message: "Lỗi dữ liệu",
        description: response.message || "Dữ liệu không hợp lệ",
      });
    }
    notification.info({
      message: "Thông báo",
      description: "Hủy lưu việc thành công",
    });
    setJobFreelancerInfo(response?.data || null);
  };

  if (!job) {
    return <LoadingEffect />;
  }

  return (
    <div className="py-12">
      <div className="container mx-auto px-4">
        <div className="max-w-[1480px] mx-auto px-4">
          {/* Header Section */}

          <FadeInWhenVisible>
            <Card className="p-6 md:p-8 mb-8">
              <div className="flex flex-col md:flex-row md:items-start justify-between gap-6">

                {/* Logo + Thông tin job */}
                <div className="flex items-start gap-3 flex-1">
                  {/* Logo công ty */}
                  <Avatar className="w-52 h-52 rounded-md border border-gray-200 dark:border-gray-700">
                    <AvatarImage
                      src={job.companyImage}
                      alt={job.companyName || "Company logo"}
                      loading="lazy"
                      className="object-cover"
                    />
                  </Avatar>

                  {/* Nội dung */}
                  <div>
                    {/* Tiêu đề */}
                    <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-gray-200 leading-tight">
                      {job.title}
                    </h1>

                    {/* Công ty */}
                    <p className="flex items-center text-sm md:text-base text-gray-500 dark:text-gray-400 mt-2">
                      <Building className="w-4 h-4 mr-2" />
                      {job.companyName || "Ẩn danh"}
                    </p>

                    {/* Địa chỉ */}
                    <p className="flex items-center text-sm md:text-base text-gray-500 dark:text-gray-400 mt-2">
                      <MapPin className="w-4 h-4 mr-2" />
                      {job.companyAddress || "Không rõ địa chỉ"}
                    </p>

                    {/* Người đăng */}
                    <Link
                      to={`/clients/${job.clientId}`}
                      className="flex items-center text-sm md:text-base text-gray-500 dark:text-gray-400 mt-2 hover:text-primary transition-colors"
                    >
                      <User className="w-4 h-4 mr-2" />
                      <span>{job.firstName + " " + job.lastName}</span>
                    </Link>

                    {/* Badge */}
                    <div className="flex flex-col gap-2 mt-4">
                      {/* Hàng 1: type + status */}
                      <div className="flex gap-2">
                        <Badge variant="secondary" className="text-xs md:text-sm">
                          {job.type}
                        </Badge>
                        {job.status && (
                          <Badge
                            variant="default"
                            className={`text-xs md:text-sm 
                            ${job.status === "Mở"
                                ? "bg-green-100 text-green-700 hover:bg-green-200"
                                : "bg-red-100 text-red-700 hover:bg-red-200"}`}
                          >
                            {job.status}
                          </Badge>
                        )}

                      </div>

                      {/* Hàng 2: scope + jobOpportunity */}
                      <div className="flex gap-2">
                        <Badge variant="secondary" className="text-xs md:text-sm">
                          Mức độ dự án: {job.scope}
                        </Badge>

                        {job.jobOpportunity && (
                          <Badge
                            variant="default"
                            className="bg-green-100 text-green-800 hover:bg-green-200 text-xs md:text-sm"
                          >
                            Cơ hội hợp tác lâu dài
                          </Badge>
                        )}
                      </div>
                    </div>

                  </div>
                </div>

                {/* Cột nút hành động */}
                {isClient == null && (
                  <div className="flex flex-col gap-3 min-w-[250px]">
                    {/* Apply button */}
                    <Button
                      size="lg"
                      onClick={handleOpenApplyDialog}
                      disabled={
                        job.remainingTimeInHours <= 0 ||
                        ["Applied", "Approved", "Rejected", "Cancelled"].includes(jobFreelancerInfo?.status ?? "")
                      }
                      className={`w-full ${job.remainingTimeInHours <= 0 || jobFreelancerInfo?.status === "Rejected"
                        ? "bg-red-100 text-red-600 border border-red-500 cursor-not-allowed"
                        : "hover:bg-primary-700"
                        }`}>
                      {job.remainingTimeInHours <= 0
                        ? "Đã hết hạn"
                        : !jobFreelancerInfo?.status || jobFreelancerInfo?.status === "Viewed"
                          ? "Ứng tuyển ngay"
                          : jobFreelancerInfo?.status === "Applied"
                            ? "Đã ứng tuyển"
                            : jobFreelancerInfo?.status === "Approved"
                              ? "Đã chấp thuận"
                              : "Đã từ chối"}
                    </Button>

                    {/* Save/Unsave */}
                    <Button
                      variant="outline"
                      disabled={job.remainingTimeInHours <= 0}
                      onClick={
                        jobFreelancerInfo?.saved
                          ? handleUnSaveJob
                          : handleSaveJob
                      }
                      className={`w-full mt-2 ${job.remainingTimeInHours <= 0
                        ? "bg-red-100 text-red-600 border border-red-500 cursor-not-allowed"
                        : "border-green-300"
                        }`}>
                      {jobFreelancerInfo?.saved ? "Hủy lưu" : "Lưu việc làm"}
                    </Button>

                    {/* Report */}
                    <ReportDialog
                      itemId={String(id)}
                      itemType="job"
                      itemTitle={job.title}>
                      <Button
                        variant="outline"
                        size="default"
                        className="text-red-500 mt-2 border-red-500 hover:bg-red-50 hover:text-red-700 w-full flex items-center gap-2"
                      >
                        <Flag className="w-4 h-4" />
                        <span>Báo cáo</span>
                      </Button>
                    </ReportDialog>
                  </div>
                )}
              </div>
            </Card>
          </FadeInWhenVisible>

          {/* Details Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 mb-8">
            <FadeInWhenVisible delay={0.1}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-blue-50 p-3 rounded-full">
                    <CircleDollarSign className="w-6 h-6 text-blue-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Ngân sách</p>
                    <p className="font-semibold text-ms">
                      {new Intl.NumberFormat("vi-VN").format(job.fromPrice)} -{" "}
                      {new Intl.NumberFormat("vi-VN").format(job.toPrice)} VNĐ
                    </p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>

            <FadeInWhenVisible delay={0.2}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-purple-50 p-3 rounded-full">
                    <Clock className="w-6 h-6 text-purple-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Số giờ làm việc</p>
                    <p className="font-semibold text-ms">{job.hourWork} giờ</p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>

            <FadeInWhenVisible delay={0.3}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-amber-50 p-3 rounded-full">
                    <Hourglass className="w-6 h-6 text-amber-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Thời gian còn lại</p>
                    <p className="font-semibold text-ms">
                      {job.remainingTimeFormatted || "Không xác định"}
                    </p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>

            <FadeInWhenVisible delay={0.4}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-green-50 p-3 rounded-full">
                    <Calendar className="w-6 h-6 text-green-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Ngày đăng tin</p>
                    <p className="font-semibold text-ms">{job.createdTimeFormatted}</p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>

            <FadeInWhenVisible delay={0.5}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-gray-50 p-3 rounded-full">
                    <Eye className="w-6 h-6 text-gray-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Tổng lượt xem</p>
                    <p className="font-semibold text-ms">{job.totalViews} lượt</p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>

            <FadeInWhenVisible delay={0.6}>
              <Card className="p-4 flex items-center h-full">
                <div className="flex items-center gap-3 w-full">
                  <div className="bg-indigo-50 p-3 rounded-full">
                    <UserCheck className="w-6 h-6 text-indigo-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-ms text-muted-foreground">Tổng ứng viên</p>
                    <p className="font-semibold text-ms">{job.totalApplicants} người</p>
                  </div>
                </div>
              </Card>
            </FadeInWhenVisible>
          </div>

          {/* Description Section */}
          <FadeInWhenVisible delay={0.5}>
            <Card className="p-6 md:p-8 mb-8">
              <h2 className="text-xl font-semibold mb-4">Mô tả công việc</h2>
              <div className="prose max-w-none text-muted-foreground mb-6">
                {job.description}
              </div>

              <h3 className="font-semibold mb-3">Kỹ năng yêu cầu:</h3>
              <div className="flex flex-wrap gap-2 mb-6">
                {job?.skillNames?.map((skill) => (
                  <Badge key={skill} variant="secondary" className="text-xs md:text-sm">
                    {skill}
                  </Badge>
                ))}
              </div>

              {isClient == null && (
                <div className="text-center mt-6 pt-4 border-t">
                  <Button
                    size="lg"
                    onClick={handleOpenApplyDialog}
                    disabled={
                      job.remainingTimeInHours <= 0 ||
                      ["Applied", "Approved", "Rejected", "Cancelled"].includes(jobFreelancerInfo?.status ?? "")
                    }
                    className={`w-full mt-4 ${job.remainingTimeInHours <= 0 || jobFreelancerInfo?.status === "Rejected"
                      ? "bg-red-100 text-red-600 border border-red-500 cursor-not-allowed"
                      : "hover:bg-primary-700"
                      }`}
                  >
                    {job.remainingTimeInHours <= 0
                      ? "Đã hết hạn"
                      : !jobFreelancerInfo?.status || jobFreelancerInfo?.status === "Viewed"
                        ? "Ứng tuyển ngay"
                        : jobFreelancerInfo?.status === "Applied"
                          ? "Đã ứng tuyển"
                          : jobFreelancerInfo?.status === "Approved"
                            ? "Đã chấp thuận"
                            : "Đã từ chối"}
                  </Button>
                </div>
              )}
            </Card>
          </FadeInWhenVisible>
        </div>
      </div>
      <div className="container mx-auto px-4">
        {userInfo && userInfo.freelancerId && (
          <section
            className="py-10"
            id="recommended-jobs"
          >
            <div className="container mx-auto px-6">
              <FadeInWhenVisible>
                <h2 className="text-4xl font-extrabold text-center mb-10 text-gray-800 dark:text-gray-200 bg-gradient-to-r from-primary-600 to-primary-800 bg-clip-text text-transparent dark:from-primary-400 dark:to-primary-600 py-2">
                  Công Việc Phù Hợp Với Bạn
                </h2>
              </FadeInWhenVisible>
              {loadingRecommendedJobs ? (
                <div className="text-center text-gray-500 dark:text-gray-400 text-lg">
                  <div className="col-span-3 min-h-[200px] flex items-center justify-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary-600 dark:border-primary-400"></div>
                  </div>
                </div>
              ) : recommendedJobs.length === 0 ? (
                <div className="text-center text-gray-500 dark:text-gray-400 text-lg">
                  Không tìm thấy công việc phù hợp với kỹ năng của bạn.
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10 items-stretch">
                  {recommendedJobs.map((job, index) => (
                    <FadeInWhenVisible key={job.id} delay={index * 0.15}>
                      <Card className="relative p-6 bg-white dark:bg-gray-800 rounded-xl shadow-md transition-all duration-300 border border-gray-100 dark:border-gray-700 overflow-hidden group flex flex-col h-full">

                        {/* Badge góc phải */}
                        {!job.seen && (
                          <div className="absolute top-2 right-5">
                            <Badge className="bg-gradient-to-r from-blue-500 to-indigo-500 text-white text-xs font-semibold px-2 py-1 rounded-full shadow-md">
                              Mới
                            </Badge>
                          </div>
                        )}
                        {job.applied && (
                          <div className="absolute top-2 right-16">
                            <Badge className="bg-gradient-to-r from-green-500 to-emerald-500 text-white text-xs font-semibold px-2 py-1 rounded-full shadow-md">
                              Đã ứng tuyển
                            </Badge>
                          </div>
                        )}

                        {/* Header: Logo + Tiêu đề + Công ty */}
                        <div className="flex items-start gap-3 mb-4 mt-4">

                          <Avatar className="w-28 h-28 rounded-md border border-gray-200 dark:border-gray-700">
                            <AvatarImage
                              src={job.companyImage}
                              alt={job.companyName || "Company logo"}
                              loading="lazy"
                              className="object-cover"
                            />
                          </Avatar>

                          <div>
                            <h3 className="text-lg font-bold text-gray-800 dark:text-gray-200 leading-tight">
                              {job.title}
                            </h3>
                            <p className="flex items-center text-lm text-gray-500 dark:text-gray-400 mt-2">
                              <Building className="w-4 h-4 mr-2" />
                              {job.companyName || "Ẩn danh"}
                            </p>
                            <p className="flex items-center text-lm text-gray-500 dark:text-gray-400 mt-1">
                              <MapPin className="w-4 h-4 mr-2" />
                              {job.companyAddress || "Không rõ địa chỉ"}
                            </p>


                          </div>
                        </div>

                        {/* Nội dung chi tiết */}
                        <div className="flex flex-col flex-grow">
                          <p className="text-sm text-gray-600 dark:text-gray-400 mb-2">
                            <span className="font-semibold">Ngân sách: </span>
                            <span className="text-primary-600 dark:text-primary-400 font-medium">
                              {formatCurrency(job.fromPrice)} - {formatCurrency(job.toPrice)}
                            </span>
                          </p>
                          <p className="text-sm text-gray-600 dark:text-gray-400 mb-3">
                            <span className="font-semibold">Thời gian thực hiện dự án: </span>{job.hourWork} giờ
                          </p>

                          <p className="text-sm text-gray-500 dark:text-gray-400 mb-4 leading-relaxed line-clamp-3">
                            {job.description}
                          </p>

                          {/* Skills */}
                          <div className="flex flex-wrap gap-2 mb-4">
                            {job.skillName.map((skill) => (
                              <Badge
                                key={skill}
                                variant="secondary"
                                className="bg-primary-100 dark:bg-primary-900 text-primary-700 dark:text-primary-300 px-2 py-1 rounded-full text-xs font-medium"
                              >
                                {skill}
                              </Badge>
                            ))}
                          </div>

                          {/* Nút dính dưới cùng */}
                          <div className="mt-auto">
                            <Link to={`/jobs/${job.id}`}>
                              <Button
                                variant="outline"
                                className="w-full bg-primary-500 dark:bg-primary-500 text-white hover:bg-primary-700 dark:hover:bg-primary-700 border-none rounded-lg shadow-sm transition-all duration-300"
                              >
                                {job.applied ? "Xem chi tiết" : "Ứng tuyển ngay"}
                              </Button>
                            </Link>
                          </div>
                        </div>
                      </Card>
                    </FadeInWhenVisible>
                  ))}
                </div>
              )}
              {!loadingRecommendedJobs && recommendedJobs.length > 0 && (
                <div className="text-center mt-10">
                  <Button
                    onClick={() => navigate("/jobs")}
                    size="lg"
                    className="bg-primary-500 dark:bg-primary-500 hover:bg-primary-700 dark:hover:bg-primary-700 text-white"
                  >
                    Xem tất cả công việc
                  </Button>
                </div>
              )}
            </div>
          </section>
        )}
      </div>

      <Dialog open={isApplyDialogOpen} onOpenChange={setIsApplyDialogOpen}>
        <DialogContent className="w-full max-w-[1000px] max-h-[90vh] p-0 overflow-hidden">
          <div className="grid grid-cols-1 md:grid-cols-3 h-full">
            <div className="col-span-1 p-6 border-r overflow-y-auto max-h-[80vh]">
              <DialogHeader className="mb-4">
                <DialogTitle>Chọn CV để ứng tuyển</DialogTitle>
                <DialogDescription>
                  Chọn một CV để gửi kèm với đơn ứng tuyển công việc {job.title}
                </DialogDescription>
              </DialogHeader>

              {cvs.length === 0 ? (
                <div className="text-center py-6">
                  <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-primary/10 mb-4">
                    <FileText className="w-8 h-8 text-primary" />
                  </div>
                  <h3 className="text-lg font-medium mb-2">Chưa có CV nào</h3>
                  <p className="text-gray-500 mb-6">
                    Bạn chưa có CV nào trong hệ thống. Hãy tải lên CV để ứng
                    tuyển.
                  </p>
                </div>
              ) : (
                <div className="space-y-3">
                  {cvs.map((cv) => (
                    <div
                      key={cv.id}
                      className={`
                  border rounded-lg p-4 cursor-pointer transition-all flex justify-between items-center
                  ${selectedCvId === cv.id
                          ? "border-primary bg-primary/10"
                          : "hover:bg-gray-50"
                        }
                `}
                      onClick={() => setSelectedCvId(cv.id)}
                    >
                      <div className="flex items-center gap-3">
                        <FileText className="w-5 h-5 text-primary" />
                        <span className="font-medium truncate max-w-[200px]">
                          {cv.title || "CV không đặt tên"}
                        </span>
                      </div>
                      <div className="flex items-center gap-2">

                        {selectedCvId === cv.id && (
                          <CheckCircle className="w-5 h-5 text-primary" />
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}

              <div className="mt-4">
                <input
                  type="file"
                  id="cv-upload"
                  accept="application/pdf"
                  className="hidden"
                  onChange={handleUploadCV}
                  disabled={uploading}
                />
                <Button
                  variant="outline"
                  className="w-full border-green-300"
                  onClick={() => document.getElementById("cv-upload")?.click()}
                  disabled={uploading}
                >
                  {uploading ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      Đang tải lên...
                    </>
                  ) : (
                    <>
                      <Plus className="w-4 h-4 mr-2" />
                      Tải CV mới
                    </>
                  )}
                </Button>
              </div>
            </div>

            <div className="col-span-2 p-6">
              <div className="h-[70vh] border rounded-lg overflow-hidden">
                {selectedCvId ? (
                  cvPreviews[selectedCvId] ? (
                    <iframe
                      src={cvPreviews[selectedCvId]}
                      width="100%"
                      height="100%"
                      style={{
                        border: "none",
                        borderRadius: "8px",
                      }}
                      title={`CV Preview: ${cvs.find((cv) => cv.id === selectedCvId)?.title ||
                        "Untitled"
                        }`}
                    />
                  ) : (
                    <div className="flex justify-center items-center h-full">
                      <Loader2 className="w-6 h-6 animate-spin text-primary mr-2" />
                      <span>Đang tải preview...</span>
                    </div>
                  )
                ) : (
                  <div className="flex justify-center items-center h-full text-muted-foreground">
                    Chọn một CV để xem trước
                  </div>
                )}
              </div>

              <div className="flex justify-end gap-2 mt-4">
                <Button
                  variant="outline"
                  className="border-green-300"
                  onClick={() => setIsApplyDialogOpen(false)}
                >
                  Hủy
                </Button>
                <Button className="hover:bg-primary-700" onClick={handleApplyJob} disabled={!selectedCvId}>
                  Ứng tuyển
                </Button>
              </div>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default JobDetail;

