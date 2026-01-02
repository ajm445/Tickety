import { useQuery } from '@tanstack/react-query';
import { concertApi } from '../api/concertApi';

// Get open concerts (default for booking pages)
export const useConcerts = () => {
  return useQuery({
    queryKey: ['concerts'],
    queryFn: () => concertApi.getConcerts(),
    select: (response) => response.data,
  });
};

// Get all concerts (admin)
export const useAllConcerts = () => {
  return useQuery({
    queryKey: ['concerts', 'all'],
    queryFn: () => concertApi.getAllConcerts(),
    select: (response) => response.data,
  });
};

// Get upcoming concerts
export const useUpcomingConcerts = () => {
  return useQuery({
    queryKey: ['concerts', 'upcoming'],
    queryFn: () => concertApi.getUpcomingConcerts(),
    select: (response) => response.data,
  });
};

// Get single concert by ID
export const useConcert = (concertId: string | undefined) => {
  return useQuery({
    queryKey: ['concerts', concertId],
    queryFn: () => concertApi.getConcertById(concertId!),
    select: (response) => response.data,
    enabled: !!concertId,
  });
};

// Search concerts by keyword
export const useSearchConcerts = (keyword: string) => {
  return useQuery({
    queryKey: ['concerts', 'search', keyword],
    queryFn: () => concertApi.searchConcerts(keyword),
    select: (response) => response.data,
    enabled: keyword.length > 0,
  });
};
