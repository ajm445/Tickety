import { Ticket } from 'lucide-react';
import { Link } from 'react-router-dom';

export const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="mt-auto border-t border-gray-200 bg-gray-50">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 gap-8 md:grid-cols-4">
          {/* Logo & Description */}
          <div className="col-span-1 md:col-span-2">
            <Link to="/" className="flex items-center gap-2">
              <Ticket className="h-6 w-6 text-blue-600" />
              <span className="text-lg font-bold text-gray-900">Tickety</span>
            </Link>
            <p className="mt-4 text-sm text-gray-600">
              콘서트 티켓 예약을 위한 신뢰할 수 있는 플랫폼입니다.
              안전하고 편리한 서비스로 원활한 예약을 경험하세요.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="text-sm font-semibold text-gray-900">바로가기</h3>
            <ul className="mt-4 space-y-2">
              <li>
                <Link to="/concerts" className="text-sm text-gray-600 hover:text-gray-900">
                  공연 목록
                </Link>
              </li>
              <li>
                <Link to="/reservations" className="text-sm text-gray-600 hover:text-gray-900">
                  내 예약
                </Link>
              </li>
              <li>
                <Link to="/mypage" className="text-sm text-gray-600 hover:text-gray-900">
                  마이페이지
                </Link>
              </li>
            </ul>
          </div>

          {/* Support */}
          <div>
            <h3 className="text-sm font-semibold text-gray-900">고객지원</h3>
            <ul className="mt-4 space-y-2">
              <li>
                <a href="#" className="text-sm text-gray-600 hover:text-gray-900">
                  고객센터
                </a>
              </li>
              <li>
                <a href="#" className="text-sm text-gray-600 hover:text-gray-900">
                  문의하기
                </a>
              </li>
              <li>
                <a href="#" className="text-sm text-gray-600 hover:text-gray-900">
                  이용약관
                </a>
              </li>
              <li>
                <a href="#" className="text-sm text-gray-600 hover:text-gray-900">
                  개인정보처리방침
                </a>
              </li>
            </ul>
          </div>
        </div>

        {/* Copyright */}
        <div className="mt-8 border-t border-gray-200 pt-8">
          <p className="text-center text-sm text-gray-500">
            &copy; {currentYear} Tickety. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
