import apiClient from '../../../services/apiClient';
import type { ApiResponse, Concert } from '../../../types';

export const concertApi = {
  // Get all concerts
  getConcerts: async (): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get('/api/concerts');
    return response.data;
  },

  // Get concerts with status OPEN
  getOpenConcerts: async (): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get('/api/concerts/open');
    return response.data;
  },

  // Get concert by ID
  getConcertById: async (concertId: string): Promise<ApiResponse<Concert>> => {
    const response = await apiClient.get(`/api/concerts/${concertId}`);
    return response.data;
  },

  // Search concerts by keyword
  searchConcerts: async (keyword: string): Promise<ApiResponse<Concert[]>> => {
    const response = await apiClient.get(`/api/concerts/search?keyword=${encodeURIComponent(keyword)}`);
    return response.data;
  },
};
