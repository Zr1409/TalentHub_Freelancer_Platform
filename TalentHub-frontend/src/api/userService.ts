import api from "@/api/axiosConfig";

interface User {
    id: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    province: string | null;
    ward: string | null;
    title: string;
    introduction: string;
    image: string;
    role: string;
    status: string;
    mfaEnabled: boolean;
}

interface ApiResponse<T> {
    message: string;
    status: number;
    data: T | null;
}

interface ImageUploadResponse {
    url: string;
}

interface ChangePasswordRequest {
    email: string;
    currentPassword: string;
    newPassword: string;
}

interface ResetPasswordRequest {
    email: string;
    code: string;
    newPassword: string;
}

interface OtpResponse {
    message: string;
    success: boolean;
}

const userService = {
    getUserById: async (userId: number): Promise<ApiResponse<User>> => {
        try {
            const response = await api.get(`/users/${userId}`);
            return {
                message: response.data?.message,
                status: response.status,
                data: response.data,
            };
        } catch (error) {
            throw error;
        }
    },

    updateUser: async (userId: number, userData: Partial<User>): Promise<ApiResponse<User>> => {
        try {
            const response = await api.put(`/users/${userId}`, userData);
            return {
                message: response.data?.message || 'User updated successfully',
                status: response.status,
                data: response.data,
            };
        } catch (error) {
            throw error;
        }
    },


    uploadImage: async (file: File, folder = "talenthub_images"): Promise<string> => {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("folder", folder); 

        try {
            const response = await api.post("/images/upload", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            return response.url;
        } catch (error) {
            throw error;
        }
    },


    updateUserImage: async (userId: number, imageUrl: string): Promise<ApiResponse<User>> => {
        return await userService.updateUser(userId, { image: imageUrl });
    },

    changePassword: async (data: ChangePasswordRequest): Promise<any> => {
        try {
            const response = await api.post('/v1/account/change-password', data);
            return response;
        } catch (error) {
            throw error;
        }
    },

    sendOtp: async (email: string): Promise<any> => {
        try {
            const response = await api.post(`/v1/account/send-otp?email=${encodeURIComponent(email)}`);
            return response;
        } catch (error) {
            throw error;
        }
    },

    resetPassword: async (data: ResetPasswordRequest): Promise<any> => {
        try {
            const response = await api.post('/v1/account/reset-password', data);
            return response;
        } catch (error) {
            throw error;
        }
    },

    getLocationDisplay: (province: string | null, ward: string | null): string => {
        const parts = [];

        if (ward) {
            parts.push(ward);
        }

        if (province) {
            parts.push(province);
        }

        return parts.join(', ');
    }
};

export default userService;
export type {
    User,
    ApiResponse,
    ImageUploadResponse,
    ChangePasswordRequest,
    ResetPasswordRequest,
    OtpResponse
};