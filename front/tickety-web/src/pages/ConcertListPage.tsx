import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, Calendar, MapPin } from 'lucide-react';
import { Input, Button, Loading } from '../components/common';

// Mock data - will be replaced with API call
const mockConcerts = [
  {
    id: 1,
    title: '2024 여름 뮤직 페스티벌',
    venue: { name: '올림픽 경기장', address: '서울' },
    startDate: '2024-07-15',
    endDate: '2024-07-17',
    posterUrl: 'https://via.placeholder.com/400x300/3b82f6/ffffff?text=Festival',
    category: '페스티벌',
  },
  {
    id: 2,
    title: '클래식 나이트',
    venue: { name: '예술의전당', address: '서울' },
    startDate: '2024-08-01',
    endDate: '2024-08-01',
    posterUrl: 'https://via.placeholder.com/400x300/8b5cf6/ffffff?text=Classical',
    category: '클래식',
  },
  {
    id: 3,
    title: 'K-Pop 라이브 콘서트',
    venue: { name: 'COEX 홀', address: '서울' },
    startDate: '2024-09-20',
    endDate: '2024-09-22',
    posterUrl: 'https://via.placeholder.com/400x300/ec4899/ffffff?text=K-Pop',
    category: 'K-Pop',
  },
];

export const ConcertListPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isLoading] = useState(false);

  const filteredConcerts = mockConcerts.filter((concert) =>
    concert.title.toLowerCase().includes(searchQuery.toLowerCase())
  );

  if (isLoading) {
    return <Loading fullScreen text="공연 정보를 불러오는 중..." />;
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">공연 목록</h1>
        <p className="mt-2 text-gray-600">
          다가오는 공연을 확인하고 티켓을 예약하세요
        </p>
      </div>

      {/* Search */}
      <div className="mb-8">
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
          <Input
            placeholder="공연 검색..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10"
          />
        </div>
      </div>

      {/* Concert Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredConcerts.map((concert) => (
          <Link
            key={concert.id}
            to={`/concerts/${concert.id}`}
            className="group"
          >
            <div className="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-md transition-shadow border border-gray-100">
              <div className="aspect-[4/3] bg-gray-200 relative overflow-hidden">
                <img
                  src={concert.posterUrl}
                  alt={concert.title}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
                <span className="absolute top-3 right-3 bg-blue-600 text-white text-xs font-medium px-2 py-1 rounded">
                  {concert.category}
                </span>
              </div>
              <div className="p-4">
                <h3 className="font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">
                  {concert.title}
                </h3>
                <div className="mt-2 space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Calendar className="h-4 w-4" />
                    <span>{concert.startDate}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <MapPin className="h-4 w-4" />
                    <span>{concert.venue.name}, {concert.venue.address}</span>
                  </div>
                </div>
                <div className="mt-4">
                  <Button variant="outline" size="sm" className="w-full">
                    상세보기
                  </Button>
                </div>
              </div>
            </div>
          </Link>
        ))}
      </div>

      {filteredConcerts.length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500">검색 결과가 없습니다.</p>
        </div>
      )}
    </div>
  );
};

export default ConcertListPage;
