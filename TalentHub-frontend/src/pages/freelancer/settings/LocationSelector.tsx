import React, { useState, useEffect, useRef } from "react";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Check, ChevronsUpDown, Loader2, Search } from "lucide-react";
import { cn } from "@/lib/utils";
import addressService, { Province, Ward } from "@/api/addressService";
import { notification } from "antd";
import LoadingEffect from "@/components/ui/LoadingEffect";

interface LocationSelectorProps {
  provinceId: string | null; // Mặc dù UI hiển thị là Province/City nhưng vẫn giữ tên provinceId
  wardId: string | null; // Mặc dù UI hiển thị là District nhưng vẫn giữ tên wardId
  onProvinceChange: (province: string | null) => void; // Vẫn giữ tên để tương thích với code hiện tại
  onWardChange: (ward: string | null) => void; // Vẫn giữ tên để tương thích với code hiện tại
  disabled?: boolean;
}

const LocationSelector: React.FC<LocationSelectorProps> = ({
  provinceId,
  wardId,
  onProvinceChange,
  onWardChange,
  disabled = false,
}) => {
  const [provinces, setProvinces] = useState<Province[]>([]);
  const [wards, setWards] = useState<Ward[]>([]);
  const [provinceOpen, setProvinceOpen] = useState(false);
  const [wardOpen, setWardOpen] = useState(false);
  const [selectedProvince, setSelectedProvince] = useState<Province | null>(null);
  const [selectedWard, setSelectedWard] = useState<Ward | null>(null);
  const [loadingProvinces, setLoadingProvinces] = useState<boolean>(false);
  const [loadingWards, setLoadingWards] = useState(false);
  const [provinceSearchValue, setProvinceSearchValue] = useState("");
  const [wardSearchValue, setWardSearchValue] = useState("");

  // Lấy danh sách tỉnh/thành phố khi component được tạo
  useEffect(() => {
    const fetchProvinces = async () => {
      if (loadingProvinces) return;

      setLoadingProvinces(true);
      try {
        const provincesData = await addressService.getProvinces();
        setProvinces(provincesData);
      } catch (error) {
        console.error("Error fetching provinces:", error);
        notification.error({
          message: "Lỗi",
          description:
            "Không thể tải danh sách tỉnh/thành phố. Vui lòng thử lại sau.",
        });
      } finally {
        setLoadingProvinces(false);
      }
    };

    fetchProvinces();
  }, []);

  // Khôi phục dữ liệu từ các giá trị đã lưu
  useEffect(() => {
    const loadSavedLocation = async () => {
      // Nếu có provinceId (tương ứng với tỉnh/thành phố)
      if (provinceId) {
        // Tìm tỉnh/thành phố trong danh sách đã tải
        const province = provinces.find((p) => p.name === provinceId);
        // const province = provinces.find((p) => p.code.toString() === provinceId);

        if (province) {
          setSelectedProvince(province);

          // Tải danh sách quận/huyện
          if (province.code) {
            try {
              setLoadingWards(true);
              const provinceDetails = await addressService.getProvinceDetails(
                province.code.toString()
              );
              if (provinceDetails && provinceDetails.wards) {
                setWards(provinceDetails.wards);

                // Nếu có wardId (tương ứng với quận/huyện)
                if (wardId) {
                  //const ward = provinceDetails.wards.find((w) => w.code.toString() === wardId);
                  const ward = provinceDetails.wards.find((w) => w.name === wardId);

                  if (ward) {
                    setSelectedWard(ward);
                  }
                }
              }
            } catch (error) {
              console.error("Error fetching districts:", error);
            } finally {
              setLoadingWards(false);
            }
          }
        }
      }
    };

    if (provinces.length > 0) {
      loadSavedLocation();
    }
  }, [provinceId, wardId, provinces]);

  // Tải danh sách phường/xã khi chọn tỉnh/thành phố
  const fetchWardsByProvinceCode = async (provinceCode: string | number) => {
    if (!provinceCode || loadingWards) return;

    setLoadingWards(true);
    try {
      const provinceDetails = await addressService.getProvinceDetails(
        provinceCode.toString()
      );
      if (provinceDetails && provinceDetails.wards) {
        setWards(provinceDetails.wards);
      }
    } catch (error) {
      console.error(
        `Error fetching districts for province ${provinceCode}:`,
        error
      );
      notification.error({
        message: "Lỗi",
        description:
          "Không thể tải danh sách phường/xã. Vui lòng thử lại sau.",
      });
      setWards([]);
    } finally {
      setLoadingWards(false);
    }
  };

  // Chọn tỉnh
  const handleProvinceSelect = (province: Province) => {
    setSelectedProvince(province);
    //onCountryChange(province.code.toString());
    onProvinceChange(province.name);
    setProvinceOpen(false);
    setProvinceSearchValue("");
    setSelectedWard(null);
    onWardChange(null);
    setWards(province.wards || []);

    if (province.code) {
      fetchWardsByProvinceCode(province.code.toString());
    }
  };

  // Chọn xã/phường
  const handleWardSelect = (ward: Ward) => {
    setSelectedWard(ward);
    //onProvinceChange(ward.code.toString());
    onWardChange(ward.name);
    setWardOpen(false);
    setWardSearchValue("");
  };

  // Lọc danh sách tỉnh/thành phố theo từ khóa tìm kiếm
  const filteredProvinces = React.useMemo(() => {
    return provinceSearchValue.trim() === ""
      ? provinces
      : provinces.filter((province) =>
        province.name
          .toLowerCase()
          .includes(provinceSearchValue.toLowerCase())
      );
  }, [provinces, provinceSearchValue]);

  // Lọc danh sách phường/xã theo từ khóa tìm kiếm
  const filteredWards = React.useMemo(() => {
    return wardSearchValue.trim() === ""
      ? wards
      : wards.filter((ward) =>
        ward.name.toLowerCase().includes(wardSearchValue.toLowerCase())
      );
  }, [wards, wardSearchValue]);


  return (
    <div className="space-y-3">
      {/* Tỉnh/Thành phố Selector (Province/City) */}
      <div className="relative">
        <Popover open={provinceOpen} onOpenChange={setProvinceOpen}>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              role="combobox"
              className="w-full justify-between"
              disabled={disabled || loadingProvinces}
            >
              {loadingProvinces ? <LoadingEffect /> : selectedProvince?.name || "Chọn Tỉnh/Thành phố"}
              <ChevronsUpDown className="ml-2 h-4 w-4 opacity-50" />
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-full p-0">
            <div className="p-2">
              <div className="relative mb-2">
                <Search className="absolute left-2 top-2.5 h-4 w-4" />
                <Input
                  placeholder="Tìm kiếm tỉnh/thành phố"
                  className="pl-8"
                  value={provinceSearchValue}
                  onChange={(e) => setProvinceSearchValue(e.target.value)}
                />
              </div>
              {filteredProvinces.length > 0 ? (
                <div className="max-h-60 overflow-y-auto">
                  {filteredProvinces.map((province) => (
                    <div
                      key={province.code}
                      className={cn(
                        "px-2 py-1.5 cursor-pointer hover:bg-muted rounded",
                        selectedProvince?.code === province.code && "bg-muted"
                      )}
                      onClick={() => handleProvinceSelect(province)}
                    >
                      <div className="flex-shrink-0">
                        {selectedProvince?.code === province.code && (
                          <Check className="h-4 w-4" />
                        )}
                      </div>
                      <div>{province.name}</div>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-center py-4 text-sm">Không tìm thấy tỉnh/thành phố</p>
              )}
            </div>
          </PopoverContent>
        </Popover>
      </div>

      {/* Quận/Huyện Selector (District) */}
      <div className="relative">
        <Popover open={wardOpen} onOpenChange={setWardOpen}>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              role="combobox"
              className="w-full justify-between"
              disabled={disabled || !selectedProvince}
            >
              {loadingWards ? <LoadingEffect /> : selectedWard?.name || "Chọn Xã/Phường"}
              <ChevronsUpDown className="ml-2 h-4 w-4 opacity-50" />
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-full p-0">
            <div className="p-2">
              <div className="relative mb-2">
                <Search className="absolute left-2 top-2.5 h-4 w-4" />
                <Input
                  placeholder="Tìm kiếm xã/phường"
                  className="pl-8"
                  value={wardSearchValue}
                  onChange={(e) => setWardSearchValue(e.target.value)}
                />
              </div>
              {filteredWards.length > 0 ? (
                <div className="max-h-60 overflow-y-auto">
                  {filteredWards.map((ward) => (
                    <div
                      key={ward.code}
                      className={cn(
                        "px-2 py-1.5 cursor-pointer hover:bg-muted rounded",
                        selectedWard?.code === ward.code && "bg-muted"
                      )}
                      onClick={() => handleWardSelect(ward)}
                    >
                      <div className="flex-shrink-0">
                        {selectedWard?.code === ward.code && (
                          <Check className="h-4 w-4" />
                        )}
                      </div>
                      <div>{ward.name}</div>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-center py-4 text-sm text-muted-foreground">
                  {selectedProvince
                    ? "Không tìm thấy xã/phường"
                    : "Vui lòng chọn tỉnh/thành phố trước"}
                </p>
              )}
            </div>
          </PopoverContent>
        </Popover>

      </div>
    </div>
  );
};

export default LocationSelector;
