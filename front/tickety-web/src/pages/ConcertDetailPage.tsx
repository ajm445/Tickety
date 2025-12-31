import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Calendar, MapPin, Clock, Users, Ticket } from 'lucide-react';
import { Button } from '../components/common';

// Mock data - will be replaced with API call
const mockConcerts = [
  {
    id: 1,
    title: '2024 여름 뮤직 페스티벌',
    description: '올 여름 가장 핫한 아티스트들이 한자리에! 3일간 펼쳐지는 음악의 향연을 놓치지 마세요. 다양한 장르의 음악과 함께 특별한 여름밤을 만들어보세요.',
    venue: { name: '올림픽 경기장', address: '서울특별시 송파구 올림픽로 424' },
    startDate: '2024-07-15',
    endDate: '2024-07-17',
    startTime: '18:00',
    posterUrl: 'https://via.placeholder.com/800x400/3b82f6/ffffff?text=Summer+Festival+2024',
    category: '페스티벌',
    totalSeats: 50000,
    availableSeats: 12500,
    priceRange: { min: 60000, max: 150000 },
  },
  {
    id: 2,
    title: '클래식 나이트',
    description: '세계적인 오케스트라와 함께하는 클래식 음악의 밤. 베토벤, 모차르트, 쇼팽의 명곡들을 라이브로 감상하세요.',
    venue: { name: '예술의전당', address: '서울특별시 서초구 남부순환로 2406' },
    startDate: '2024-08-01',
    endDate: '2024-08-01',
    startTime: '19:30',
    posterUrl: 'https://via.placeholder.com/800x400/8b5cf6/ffffff?text=Classical+Night',
    category: '클래식',
    totalSeats: 2500,
    availableSeats: 800,
    priceRange: { min: 50000, max: 120000 },
  },
  {
    id: 3,
    title: 'K-Pop 라이브 콘서트',
    description: '최고의 K-Pop 아티스트들의 화려한 무대! 팬들과 함께하는 특별한 순간을 경험하세요.',
    venue: { name: 'COEX 홀', address: '서울특별시 강남구 영동대로 513' },
    startDate: '2024-09-20',
    endDate: '2024-09-22',
    startTime: '19:00',
    posterUrl: 'https://via.placeholder.com/800x400/ec4899/ffffff?text=K-Pop+Live',
    category: 'K-Pop',
    totalSeats: 10000,
    availableSeats: 2500,
    priceRange: { min: 80000, max: 180000 },
  },
];

export const ConcertDetailPage = () => {
  const { concertId } = useParams<{ concertId: string }>();
  const navigate = useNavigate();

  const concert = mockConcerts.find((c) => c.id === Number(concertId));

  if (!concert) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div className="text-center py-12">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">공연을 찾을 수 없습니다</h2>
          <p className="text-gray-600 mb-8">요청하신 공연 정보가 존재하지 않습니다.</p>
          <Button onClick={() => navigate('/concerts')}>공연 목록으로 돌아가기</Button>
        </div>
      </div>
    );
  }

  const availabilityPercentage = Math.round((concert.availableSeats / concert.totalSeats) * 100);

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

      {/* Hero Section */}
      <div className="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100 mb-8">
        <div className="aspect-[2/1] md:aspect-[3/1] bg-gray-200 relative">
          <img
            src={concert.posterUrl}
            alt={concert.title}
            className="w-full h-full object-cover"
          />
          <span className="absolute top-4 left-4 bg-blue-600 text-white text-sm font-medium px-3 py-1 rounded-full">
            {concert.category}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-8">
          {/* Title & Description */}
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-4">{concert.title}</h1>
            <p className="text-gray-600 leading-relaxed">{concert.description}</p>
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
                    {concert.startDate === concert.endDate
                      ? concert.startDate
                      : `${concert.startDate} ~ ${concert.endDate}`}
                  </p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <Clock className="h-5 w-5 text-blue-600 mt-0.5" />
                <div>
                  <p className="font-medium text-gray-900">공연 시간</p>
                  <p className="text-gray-600">{concert.startTime} 시작</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <MapPin className="h-5 w-5 text-blue-600 mt-0.5" />
                <div>
                  <p className="font-medium text-gray-900">공연 장소</p>
                  <p className="text-gray-600">{concert.venue.name}</p>
                  <p className="text-gray-500 text-sm">{concert.venue.address}</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <Users className="h-5 w-5 text-blue-600 mt-0.5" />
                <div>
                  <p className="font-medium text-gray-900">잔여 좌석</p>
                  <p className="text-gray-600">
                    {concert.availableSeats.toLocaleString()}석 / {concert.totalSeats.toLocaleString()}석
                  </p>
                </div>
              </div>
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
                {concert.priceRange.min.toLocaleString()}원 ~ {concert.priceRange.max.toLocaleString()}원
              </p>
            </div>

            {/* Availability */}
            <div className="mb-6">
              <div className="flex justify-between text-sm mb-2">
                <span className="text-gray-600">잔여 좌석</span>
                <span className="font-medium text-gray-900">{availabilityPercentage}% 남음</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className={`h-2 rounded-full ${
                    availabilityPercentage > 50
                      ? 'bg-green-500'
                      : availabilityPercentage > 20
                      ? 'bg-yellow-500'
                      : 'bg-red-500'
                  }`}
                  style={{ width: `${availabilityPercentage}%` }}
                />
              </div>
            </div>

            {/* Booking Button */}
            <Link to={`/concerts/${concert.id}/reserve`}>
              <Button className="w-full" size="lg">
                <Ticket className="h-5 w-5 mr-2" />
                좌석 선택하기
              </Button>
            </Link>

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
