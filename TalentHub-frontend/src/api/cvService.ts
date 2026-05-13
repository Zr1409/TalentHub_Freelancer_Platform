import api from "@/api/axiosConfig";

export interface JobApplication {
    jobId: number;
    jobTitle: string;
    companyName: string;
    status: string;
}

export interface CV {
    id: number;
    title: string;
    url: string;
    status: boolean;
    jobs?: JobApplication[];
    createdAt?: string;
    updatedAt?: string;
}

export interface ApiResponse<T> {
    message: string;
    status: number;
    data: T | null;
}

const cvService = {
    uploadCV: async (file: File, freelancerId: number): Promise<ApiResponse<string>> => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('freelancerId', freelancerId.toString());

        const response = await api.post('/pdf/cloudinary/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        return {
            message: response.message || 'Upload CV thành công',
            status: response.status,
            data: response.url
        };
    },

    getCVsByFreelancerId: async (freelancerId: number): Promise<ApiResponse<CV[]>> => {
        const response = await api.get(`/pdf/cloudinary/freelancer/${freelancerId}`);
        return {
            message: response.message || 'Lấy danh sách CV thành công',
            status: response.status,
            data: response.data
        };
    },

    deleteCV: async (cvId: number): Promise<ApiResponse<boolean>> => {
        const response = await api.delete(`/cloudinary/pdf/${cvId}`);
        return {
            message: response.message || 'Xóa CV thành công',
            status: response.status,
            data: true
        };
    },

    downloadCV: async (publicId: string): Promise<Blob> => {
        const response = await api.get(`/pdf/cloudinary/download?publicId=${encodeURIComponent(publicId)}`, {
            responseType: 'blob',
        });
        return response;
    },

    getPreviewUrl: (publicId: string): string => {
        return `${api.defaults.baseURL}/pdf/cloudinary/download?publicId=${encodeURIComponent(publicId)}`;
    },

    previewCV: async (publicId: string): Promise<string> => {
        try {
            const blob = await cvService.downloadCV(publicId);
            return URL.createObjectURL(blob);
        } catch (error) {
            console.error('Lỗi khi tạo preview CV:', error);
            throw error;
        }
    }
};

export default cvService;