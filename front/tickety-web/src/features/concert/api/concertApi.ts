import apiClient from '../../../services/apiClient';
import type { ApiResponse, Concert } from '../../../types';

export const concertApi = {
  // Get open concerts (default - for booking)
  getConcerts: async (): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get('/api/concerts');
    return response.data;
  },

  // Get all concerts (admin)
  getAllConcerts: async (): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get('/api/concerts/all');
    return response.data;
  },

  // Get upcoming concerts
  getUpcomingConcerts: async (): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get('/api/concerts/upcoming');
    return response.data;
  },

  // Get concert by ID
  getConcertById: async (concertId: string): Promise<ApiResponse<Concert>> => {
    const response = await apiClient.get(`/api/concerts/${concertId}`);
    return response.data;
  },

  // Search concerts by keyword
  searchConcerts: async (keyword: string): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get(`/api/concerts?keyword=${encodeURIComponent(keyword)}`);
    return response.data;
  },
};
