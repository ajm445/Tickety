import { Link } from 'react-router-dom';
import { Ticket, Calendar, Shield, Zap } from 'lucide-react';
import { Button } from '../components/common';

const features = [
  {
    icon: Calendar,
    title: '간편한 예약',
    description: '몇 번의 클릭만으로 원하는 공연 티켓을 예약하세요.',
  },
  {
    icon: Shield,
    title: '안전한 결제',
    description: '기업 수준의 보안으로 안전하게 결제하세요.',
  },
  {
    icon: Zap,
    title: '실시간 업데이트',
    description: '좌석 현황을 실시간으로 확인할 수 있습니다.',
  },
];

export const HomePage = () => {
  return (
    <div className="flex flex-col">
      {/* Hero Section */}
      <section className="relative bg-gradient-to-br from-blue-600 to-blue-800 py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <div className="flex justify-center mb-6">
              <Ticket className="h-16 w-16 text-white" />
            </div>
            <h1 className="text-4xl font-bold text-white sm:text-5xl lg:text-6xl">
              Tickety에 오신 것을 환영합니다
            </h1>
            <p className="mt-6 text-xl text-blue-100 max-w-2xl mx-auto">
              콘서트 티켓 예약을 위한 원스톱 플랫폼입니다.
              실시간 좌석 선택으로 원활한 예약을 경험하세요.
            </p>
            <div className="mt-10 flex justify-center gap-4">
              <Link to="/concerts">
                <button className="inline-flex items-center justify-center gap-2 rounded-lg font-medium px-6 py-3 text-base border-2 border-white text-white hover:bg-white hover:text-blue-600 transition-all duration-200">
                  공연 둘러보기
                </button>
              </Link>
              <Link to="/auth/signup">
                <button className="inline-flex items-center justify-center gap-2 rounded-lg font-medium px-6 py-3 text-base border-2 border-white text-white hover:bg-white hover:text-blue-600 transition-all duration-200">
                  시작하기
                </button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-gray-50">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold text-gray-900">왜 Tickety인가요?</h2>
            <p className="mt-4 text-lg text-gray-600">
              최고의 콘서트 티켓 예약 경험을 제공합니다.
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {features.map((feature) => (
              <div
                key={feature.title}
                className="bg-white rounded-xl p-8 shadow-sm hover:shadow-md transition-shadow border border-gray-100"
              >
                <div className="inline-flex items-center justify-center w-12 h-12 rounded-lg bg-blue-100 text-blue-600 mb-6">
                  <feature.icon className="h-6 w-6" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">
                  {feature.title}
                </h3>
                <p className="text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-white">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="bg-blue-600 rounded-2xl p-12 text-center">
            <h2 className="text-3xl font-bold text-white mb-4">
              다음 공연을 예약할 준비가 되셨나요?
            </h2>
            <p className="text-blue-100 mb-8 max-w-2xl mx-auto">
              Tickety를 신뢰하는 수천 명의 음악 팬들과 함께하세요.
            </p>
            <Link to="/concerts">
              <button className="inline-flex items-center justify-center gap-2 rounded-lg font-medium px-6 py-3 text-base border-2 border-white text-white hover:bg-white hover:text-blue-600 transition-all duration-200">
                공연 탐색하기
              </button>
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
