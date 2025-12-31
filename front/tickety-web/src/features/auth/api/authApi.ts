import apiClient from '../../../services/apiClient';
import type { ApiResponse, LoginRequest, SignupRequest, TokenResponse, User } from '../../../types';

export const authApi = {
  login: async (data: LoginRequest): Promise<ApiResponse<TokenResponse>> => {
    const response = await apiClient.post('/api/auth/login', data);
    return response.data;
  },

  signup: async (data: SignupRequest): Promise<ApiResponse<User>> => {
    const response = await apiClient.post('/api/auth/signup', data);
    return response.data;
  },

  logout: async (): Promise<ApiResponse<null>> => {
    const response = await apiClient.post('/api/auth/logout');
    return response.data;
  },

  getMe: async (): Promise<ApiResponse<User>> => {
    const response = await apiClient.get('/api/auth/me');
    return response.data;
  },

  refreshToken: async (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> => {
    const response = await apiClient.post('/api/auth/refresh', { refreshToken });
    return response.data;
  },
};
