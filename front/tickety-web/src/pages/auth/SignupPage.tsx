import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Ticket, X } from 'lucide-react';
import { Button, Input } from '../../components/common';
import { useSignup } from '../../features/auth';

const signupSchema = z.object({
  name: z.string().min(2, '이름은 2자 이상이어야 합니다'),
  email: z.string().email('올바른 이메일을 입력해주세요'),
  password: z.string().min(6, '비밀번호는 6자 이상이어야 합니다'),
  confirmPassword: z.string(),
}).refine((data) => data.password === data.confirmPassword, {
  message: '비밀번호가 일치하지 않습니다',
  path: ['confirmPassword'],
});

type SignupFormData = z.infer<typeof signupSchema>;

export const SignupPage = () => {
  const navigate = useNavigate();
  const signup = useSignup();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignupFormData>({
    resolver: zodResolver(signupSchema),
  });

  const onSubmit = (data: SignupFormData) => {
    signup.mutate({
      name: data.name,
      email: data.email,
      password: data.password,
    });
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
          <h2 className="mt-6 text-3xl font-bold text-gray-900">회원가입</h2>
          <p className="mt-2 text-gray-600">
            지금 가입하고 콘서트 티켓을 예약하세요
          </p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-8 border border-gray-100">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <Input
              label="이름"
              placeholder="홍길동"
              error={errors.name?.message}
              {...register('name')}
            />

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

            <Input
              label="비밀번호 확인"
              type="password"
              placeholder="비밀번호를 다시 입력하세요"
              error={errors.confirmPassword?.message}
              {...register('confirmPassword')}
            />

            {signup.isError && (
              <div className="text-sm text-red-600 bg-red-50 p-3 rounded-lg">
                회원가입에 실패했습니다. 다시 시도해주세요.
              </div>
            )}

            <Button
              type="submit"
              className="w-full"
              isLoading={signup.isPending}
            >
              가입하기
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            이미 계정이 있으신가요?{' '}
            <Link
              to="/auth/login"
              className="text-blue-600 hover:text-blue-700 font-medium"
            >
              로그인
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignupPage;
