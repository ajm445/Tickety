import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, Calendar, MapPin } from 'lucide-react';
import { Input, Button, Loading } from '../components/common';
import { useConcerts } from '../features/concert';
import type { Concert } from '../types';

// Sample data for fallback when API is not available
const sampleConcert: Concert = {
  id: 'sample-1',
  venueId: 'venue-1',
  venue: {
    id: 'venue-1',
    name: '올림픽 경기장',
    address: '올림픽로 424',
    city: '서울',
    totalSeats: 50000,
  },
  title: '2025 신년 콘서트',
  artist: '서울 필하모닉 오케스트라',
  description: '새해를 맞이하는 특별한 클래식 공연',
  concertDate: '2025-01-15T19:00:00',
  bookingStartAt: '2024-12-20T10:00:00',
  bookingEndAt: '2025-01-15T18:00:00',
  status: 'OPEN',
  posterUrl: 'https://via.placeholder.com/400x300/3b82f6/ffffff?text=New+Year+Concert',
  priceMin: 50000,
  priceMax: 150000,
};

export const ConcertListPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const { data: concerts, isLoading, error } = useConcerts();

  // Use API data or fallback to sample data
  const displayConcerts = concerts && concerts.length > 0 ? concerts : [sampleConcert];

  const filteredConcerts = displayConcerts.filter((concert) =>
    concert.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    concert.artist.toLowerCase().includes(searchQuery.toLowerCase())
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

      {/* API Error Notice */}
      {error && (
        <div className="mb-4 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
          <p className="text-yellow-800 text-sm">
            서버 연결에 실패했습니다. 샘플 데이터를 표시합니다.
          </p>
        </div>
      )}

      {/* Search */}
      <div className="mb-8">
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
          <Input
            placeholder="공연 또는 아티스트 검색..."
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
                  src={concert.posterUrl || 'https://via.placeholder.com/400x300/6b7280/ffffff?text=No+Image'}
                  alt={concert.title}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
                <span className={`absolute top-3 right-3 text-white text-xs font-medium px-2 py-1 rounded ${
                  concert.status === 'OPEN' ? 'bg-green-600' :
                  concert.status === 'SOLD_OUT' ? 'bg-red-600' :
                  concert.status === 'SCHEDULED' ? 'bg-blue-600' :
                  'bg-gray-600'
                }`}>
                  {concert.status === 'OPEN' ? '예매 중' :
                   concert.status === 'SOLD_OUT' ? '매진' :
                   concert.status === 'SCHEDULED' ? '예정' :
                   concert.status === 'COMPLETED' ? '종료' :
                   concert.status === 'CANCELLED' ? '취소' : concert.status}
                </span>
              </div>
              <div className="p-4">
                <h3 className="font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">
                  {concert.title}
                </h3>
                <p className="text-sm text-gray-500 mt-1">{concert.artist}</p>
                <div className="mt-2 space-y-1">
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Calendar className="h-4 w-4" />
                    <span>{new Date(concert.concertDate).toLocaleDateString('ko-KR')}</span>
                  </div>
                  {concert.venue && (
                    <div className="flex items-center gap-2 text-sm text-gray-600">
                      <MapPin className="h-4 w-4" />
                      <span>{concert.venue.name}, {concert.venue.city}</span>
                    </div>
                  )}
                </div>
                <div className="mt-3 text-sm font-medium text-blue-600">
                  {concert.priceMin.toLocaleString()}원 ~ {concert.priceMax.toLocaleString()}원
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
