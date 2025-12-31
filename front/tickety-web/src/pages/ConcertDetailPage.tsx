import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Calendar, MapPin, Clock, Users, Ticket, AlertCircle } from 'lucide-react';
import { Button, Loading } from '../components/common';
import { useConcert } from '../features/concert';
import type { Concert } from '../types';

// Sample data for fallback when API is not available
const sampleConcert: Concert = {
  id: 'sample-1',
  venueId: 'venue-1',
  venue: {
    id: 'venue-1',
    name: '올림픽 경기장',
    address: '서울특별시 송파구 올림픽로 424',
    city: '서울',
    totalSeats: 50000,
    description: '대한민국 최대 규모의 다목적 경기장',
  },
  title: '2025 신년 콘서트',
  artist: '서울 필하모닉 오케스트라',
  description: '새해를 맞이하는 특별한 클래식 공연입니다. 베토벤 교향곡 9번 "합창"을 포함한 명곡들을 라이브로 감상하세요. 세계적인 지휘자와 함께하는 감동적인 무대를 놓치지 마세요.',
  concertDate: '2025-01-15T19:00:00',
  bookingStartAt: '2024-12-20T10:00:00',
  bookingEndAt: '2025-01-15T18:00:00',
  status: 'OPEN',
  posterUrl: 'https://via.placeholder.com/800x400/3b82f6/ffffff?text=New+Year+Concert+2025',
  priceMin: 50000,
  priceMax: 150000,
};

export const ConcertDetailPage = () => {
  const { concertId } = useParams<{ concertId: string }>();
  const navigate = useNavigate();
  const { data: concert, isLoading, error } = useConcert(concertId);

  // Use API data or fallback to sample data for demo
  const displayConcert = concert || (concertId?.startsWith('sample') ? sampleConcert : null);

  if (isLoading) {
    return <Loading fullScreen text="공연 정보를 불러오는 중..." />;
  }

  if (!displayConcert) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="text-center py-12">
          <AlertCircle className="h-12 w-12 text-red-500 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-gray-900 mb-4">공연을 찾을 수 없습니다</h2>
          <p className="text-gray-600 mb-8">요청하신 공연 정보가 존재하지 않습니다.</p>
          <Button onClick={() => navigate('/concerts')}>공연 목록으로 돌아가기</Button>
        </div>
      </div>
    );
  }

  const concertDate = new Date(displayConcert.concertDate);
  const isBookingOpen = displayConcert.status === 'OPEN';

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      {/* Back Button */}
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6"
      >
        <ArrowLeft className="h-4 w-4" />
        뒤로가기
      </button>

      {/* API Error Notice */}
      {error && (
        <div className="mb-4 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
          <p className="text-yellow-800 text-sm">
            서버 연결에 실패했습니다. 샘플 데이터를 표시합니다.
          </p>
        </div>
      )}

      {/* Hero Section */}
      <div className="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 mb-8">
        <div className="aspect-[2/1] md:aspect-[3/1] bg-gray-200 relative">
          <img
            src={displayConcert.posterUrl || 'https://via.placeholder.com/800x400/6b7280/ffffff?text=No+Image'}
            alt={displayConcert.title}
            className="w-full h-full object-cover"
          />
          <span className={`absolute top-4 left-4 text-white text-sm font-medium px-3 py-1 rounded-full ${
            displayConcert.status === 'OPEN' ? 'bg-green-600' :
            displayConcert.status === 'SOLD_OUT' ? 'bg-red-600' :
            displayConcert.status === 'SCHEDULED' ? 'bg-blue-600' :
            'bg-gray-600'
          }`}>
            {displayConcert.status === 'OPEN' ? '예매 중' :
             displayConcert.status === 'SOLD_OUT' ? '매진' :
             displayConcert.status === 'SCHEDULED' ? '예정' :
             displayConcert.status === 'COMPLETED' ? '종료' :
             displayConcert.status === 'CANCELLED' ? '취소' : displayConcert.status}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-8">
          {/* Title & Description */}
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{displayConcert.title}</h1>
            <p className="text-lg text-gray-600 mb-4">{displayConcert.artist}</p>
            <p className="text-gray-600 leading-relaxed">{displayConcert.description}</p>
          </div>

          {/* Details */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">공연 정보</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="flex items-start gap-3">
                <Calendar className="h-5 w-5 text-blue-600 mt-0.5" />
                <div>
                  <p className="font-medium text-gray-900">공연 일정</p>
                  <p className="text-gray-600">
                    {concertDate.toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      weekday: 'long',
                    })}
                  </p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <Clock className="h-5 w-5 text-blue-600 mt-0.5" />
                <div>
                  <p className="font-medium text-gray-900">공연 시간</p>
                  <p className="text-gray-600">
                    {concertDate.toLocaleTimeString('ko-KR', {
                      hour: '2-digit',
                      minute: '2-digit',
                    })} 시작
                  </p>
                </div>
              </div>
              {displayConcert.venue && (
                <div className="flex items-start gap-3">
                  <MapPin className="h-5 w-5 text-blue-600 mt-0.5" />
                  <div>
                    <p className="font-medium text-gray-900">공연 장소</p>
                    <p className="text-gray-600">{displayConcert.venue.name}</p>
                    <p className="text-gray-500 text-sm">{displayConcert.venue.address}</p>
                  </div>
                </div>
              )}
              {displayConcert.venue && (
                <div className="flex items-start gap-3">
                  <Users className="h-5 w-5 text-blue-600 mt-0.5" />
                  <div>
                    <p className="font-medium text-gray-900">수용 인원</p>
                    <p className="text-gray-600">
                      {displayConcert.venue.totalSeats.toLocaleString()}석
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Sidebar - Booking Card */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 sticky top-24">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">티켓 예매</h2>

            {/* Price */}
            <div className="mb-4">
              <p className="text-sm text-gray-500">가격</p>
              <p className="text-2xl font-bold text-gray-900">
                {displayConcert.priceMin.toLocaleString()}원 ~ {displayConcert.priceMax.toLocaleString()}원
              </p>
            </div>

            {/* Booking Period */}
            <div className="mb-6 p-3 bg-gray-50 rounded-lg">
              <p className="text-sm text-gray-600">
                예매 기간: {new Date(displayConcert.bookingStartAt).toLocaleDateString('ko-KR')} ~ {new Date(displayConcert.bookingEndAt).toLocaleDateString('ko-KR')}
              </p>
            </div>

            {/* Booking Button */}
            {isBookingOpen ? (
              <Link to={`/concerts/${displayConcert.id}/reserve`}>
                <Button className="w-full" size="lg">
                  <Ticket className="h-5 w-5 mr-2" />
                  좌석 선택하기
                </Button>
              </Link>
            ) : (
              <Button className="w-full" size="lg" disabled>
                {displayConcert.status === 'SOLD_OUT' ? '매진되었습니다' :
                 displayConcert.status === 'SCHEDULED' ? '예매 준비 중' :
                 displayConcert.status === 'COMPLETED' ? '공연 종료' :
                 '예매 불가'}
              </Button>
            )}

            <p className="text-xs text-gray-500 text-center mt-4">
              예약 후 10분 내 결제를 완료해야 합니다
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ConcertDetailPage;
