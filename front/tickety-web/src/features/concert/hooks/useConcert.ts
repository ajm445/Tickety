import { useQuery } from '@tanstack/react-query';
import { concertApi } from '../api/concertApi';

export const useConcerts = () => {
  return useQuery({
    queryKey: ['concerts'],
    queryFn: () => concertApi.getConcerts(),
    select: (response) => response.data,
  });
};

export const useOpenConcerts = () => {
  return useQuery({
    queryKey: ['concerts', 'open'],
    queryFn: () => concertApi.getOpenConcerts(),
    select: (response) => response.data,
  });
};

export const useConcert = (concertId: string | undefined) => {
  return useQuery({
    queryKey: ['concerts', concertId],
    queryFn: () => concertApi.getConcertById(concertId!),
    select: (response) => response.data,
    enabled: !!concertId,
  });
};

export const useSearchConcerts = (keyword: string) => {
  return useQuery({
    queryKey: ['concerts', 'search', keyword],
    queryFn: () => concertApi.searchConcerts(keyword),
    select: (response) => response.data,
    enabled: keyword.length > 0,
  });
};
