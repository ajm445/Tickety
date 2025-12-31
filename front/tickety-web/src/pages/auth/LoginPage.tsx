import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Ticket, X } from 'lucide-react';
import { Button, Input } from '../../components/common';
import { useLogin } from '../../features/auth';

const loginSchema = z.object({
  email: z.string().email('올바른 이메일을 입력해주세요'),
  password: z.string().min(6, '비밀번호는 6자 이상이어야 합니다'),
});

type LoginFormData = z.infer<typeof loginSchema>;

export const LoginPage = () => {
  const navigate = useNavigate();
  const login = useLogin();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = (data: LoginFormData) => {
    login.mutate(data);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 relative">
      {/* Close Button */}
      <button
        onClick={() => navigate('/')}
        className="absolute top-6 right-6 p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full transition-colors"
        aria-label="닫기"
      >
        <X className="h-6 w-6" />
      </button>

      <div className="max-w-md w-full">
        <div className="text-center mb-8">
          <Link to="/" className="inline-flex items-center gap-2">
            <Ticket className="h-10 w-10 text-blue-600" />
            <span className="text-2xl font-bold text-gray-900">Tickety</span>
          </Link>
          <h2 className="mt-6 text-3xl font-bold text-gray-900">다시 오신 것을 환영합니다</h2>
          <p className="mt-2 text-gray-600">
            계정에 로그인하여 계속하세요
          </p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-8 border border-gray-100">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <Input
              label="이메일"
              type="email"
              placeholder="example@email.com"
              error={errors.email?.message}
              {...register('email')}
            />

            <Input
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력하세요"
              error={errors.password?.message}
              {...register('password')}
            />

            {login.isError && (
              <div className="text-sm text-red-600 bg-red-50 p-3 rounded-lg">
                로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.
              </div>
            )}

            <Button
              type="submit"
              className="w-full"
              isLoading={login.isPending}
            >
              로그인
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            계정이 없으신가요?{' '}
            <Link
              to="/auth/signup"
              className="text-blue-600 hover:text-blue-700 font-medium"
            >
              회원가입
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
