import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api/authApi';
import { useAuthStore } from '../../../store/authStore';
import type { LoginRequest, SignupRequest } from '../../../types';

export const useLogin = () => {
  const navigate = useNavigate();
  const { setTokens, setUser } = useAuthStore();

  return useMutation({
    mutationFn: (data: LoginRequest) => authApi.login(data),
    onSuccess: async (response) => {
      const { accessToken, refreshToken } = response.data;
      setTokens(accessToken, refreshToken);

      // Fetch user info
      const userResponse = await authApi.getMe();
      setUser(userResponse.data);

      navigate('/');
    },
  });
};

export const useSignup = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: SignupRequest) => authApi.signup(data),
    onSuccess: () => {
      navigate('/auth/login');
    },
  });
};

export const useLogout = () => {
  const { logout } = useAuthStore();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: () => authApi.logout(),
    onSuccess: () => {
      logout();
      navigate('/');
    },
    onError: () => {
      // Even if API fails, clear local state
      logout();
      navigate('/');
    },
  });
};

export const useCurrentUser = () => {
  const { isAuthenticated } = useAuthStore();

  return useQuery({
    queryKey: ['currentUser'],
    queryFn: () => authApi.getMe(),
    enabled: isAuthenticated,
    select: (response) => response.data,
  });
};
