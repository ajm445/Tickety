import { User, Mail, Ticket, CreditCard } from 'lucide-react';
import { Button } from '../components/common';
import { useAuthStore } from '../store/authStore';
import { useLogout } from '../features/auth';
import { Link } from 'react-router-dom';

export const MyPage = () => {
  const { user } = useAuthStore();
  const logout = useLogout();

  const menuItems = [
    {
      icon: Ticket,
      label: '내 예약',
      description: '예약 내역을 확인하고 관리하세요',
      to: '/reservations',
    },
    {
      icon: CreditCard,
      label: '결제 내역',
      description: '결제 내역을 확인하세요',
      to: '/payments',
    },
  ];

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">마이페이지</h1>
        <p className="mt-2 text-gray-600">계정 정보와 설정을 관리하세요</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Profile Card */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <div className="text-center">
              <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-blue-100 text-blue-600 mb-4">
                <User className="h-10 w-10" />
              </div>
              <h2 className="text-xl font-semibold text-gray-900">
                {user?.name || '사용자'}
              </h2>
              <div className="flex items-center justify-center gap-2 mt-2 text-gray-600">
                <Mail className="h-4 w-4" />
                <span className="text-sm">{user?.email || 'email@example.com'}</span>
              </div>
            </div>

            <div className="mt-6 pt-6 border-t">
              <Button
                variant="outline"
                className="w-full"
                onClick={() => logout.mutate()}
                isLoading={logout.isPending}
              >
                로그아웃
              </Button>
            </div>
          </div>
        </div>

        {/* Menu Items */}
        <div className="lg:col-span-2">
          <div className="space-y-4">
            {menuItems.map((item) => (
              <Link
                key={item.to}
                to={item.to}
                className="block bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition-shadow border border-gray-100"
              >
                <div className="flex items-center gap-4">
                  <div className="flex-shrink-0">
                    <div className="inline-flex items-center justify-center w-12 h-12 rounded-lg bg-blue-100 text-blue-600">
                      <item.icon className="h-6 w-6" />
                    </div>
                  </div>
                  <div>
                    <h3 className="font-semibold text-gray-900">{item.label}</h3>
                    <p className="text-sm text-gray-600">{item.description}</p>
                  </div>
                </div>
              </Link>
            ))}
          </div>

          {/* Account Settings */}
          <div className="mt-8 bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              계정 설정
            </h3>
            <div className="space-y-4">
              <div className="flex justify-between items-center py-3 border-b">
                <div>
                  <p className="font-medium text-gray-900">이메일</p>
                  <p className="text-sm text-gray-600">{user?.email}</p>
                </div>
                <Button variant="ghost" size="sm">수정</Button>
              </div>
              <div className="flex justify-between items-center py-3 border-b">
                <div>
                  <p className="font-medium text-gray-900">비밀번호</p>
                  <p className="text-sm text-gray-600">••••••••</p>
                </div>
                <Button variant="ghost" size="sm">변경</Button>
              </div>
              <div className="flex justify-between items-center py-3">
                <div>
                  <p className="font-medium text-gray-900">알림 설정</p>
                  <p className="text-sm text-gray-600">이메일 알림 활성화됨</p>
                </div>
                <Button variant="ghost" size="sm">설정</Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyPage;
