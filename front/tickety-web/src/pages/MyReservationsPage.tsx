import { Link } from 'react-router-dom';
import { Calendar, Clock, Ticket, AlertCircle } from 'lucide-react';
import { Button, Loading } from '../components/common';
import { useMyReservations, useCancelReservation } from '../features/reservation';
import type { ReservationStatus } from '../types';

const statusColors: Record<ReservationStatus, string> = {
  PENDING: 'bg-yellow-100 text-yellow-800',
  CONFIRMED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-gray-100 text-gray-800',
  EXPIRED: 'bg-red-100 text-red-800',
};

const statusLabels: Record<ReservationStatus, string> = {
  PENDING: '결제 대기',
  CONFIRMED: '예약 확정',
  CANCELLED: '취소됨',
  EXPIRED: '만료됨',
};

export const MyReservationsPage = () => {
  const { data: reservations, isLoading, error } = useMyReservations();
  const cancelReservation = useCancelReservation();

  const handleCancel = (reservationId: string) => {
    if (window.confirm('예약을 취소하시겠습니까?')) {
      cancelReservation.mutate(reservationId);
    }
  };

  if (isLoading) {
    return <Loading fullScreen text="예약 정보를 불러오는 중..." />;
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[50vh]">
        <AlertCircle className="h-12 w-12 text-red-500 mb-4" />
        <p className="text-gray-600">예약 정보를 불러오는데 실패했습니다.</p>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">내 예약</h1>
        <p className="mt-2 text-gray-600">
          콘서트 티켓 예약을 확인하고 관리하세요
        </p>
      </div>

      {!reservations || reservations.length === 0 ? (
        <div className="text-center py-12 bg-white rounded-xl shadow-sm border border-gray-100">
          <Ticket className="h-12 w-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            예약 내역이 없습니다
          </h3>
          <p className="text-gray-600 mb-6">
            공연 목록에서 원하는 공연을 예약해보세요
          </p>
          <Link to="/concerts">
            <Button>공연 둘러보기</Button>
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {reservations.map((reservation) => (
            <div
              key={reservation.id}
              className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
            >
              <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <span className="text-lg font-semibold text-gray-900">
                      {reservation.concertTitle || '공연'}
                    </span>
                    <span
                      className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[reservation.status]}`}
                    >
                      {statusLabels[reservation.status]}
                    </span>
                  </div>

                  <div className="flex flex-wrap gap-4 text-sm text-gray-600">
                    <div className="flex items-center gap-1">
                      <Ticket className="h-4 w-4" />
                      <span>예약번호: {reservation.reservationNumber}</span>
                    </div>
                    {reservation.seats && reservation.seats.length > 0 && (
                      <div className="flex items-center gap-1">
                        <Ticket className="h-4 w-4" />
                        <span>
                          좌석: {reservation.seats.map(s => s.fullSeatNumber).join(', ')}
                        </span>
                      </div>
                    )}
                    <div className="flex items-center gap-1">
                      <Calendar className="h-4 w-4" />
                      <span>
                        예약일: {new Date(reservation.createdAt).toLocaleString('ko-KR')}
                      </span>
                    </div>
                    {reservation.status === 'PENDING' && reservation.expiresAt && (
                      <div className="flex items-center gap-1 text-yellow-600">
                        <Clock className="h-4 w-4" />
                        <span>
                          만료: {new Date(reservation.expiresAt).toLocaleString('ko-KR')}
                        </span>
                      </div>
                    )}
                  </div>

                  <div className="mt-2 text-sm font-medium text-gray-900">
                    총 금액: {reservation.totalAmount.toLocaleString()}원
                  </div>
                </div>

                <div className="flex gap-2">
                  {reservation.status === 'PENDING' && (
                    <>
                      <Link to={`/payment/${reservation.id}`}>
                        <Button size="sm">결제하기</Button>
                      </Link>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleCancel(reservation.id)}
                        isLoading={cancelReservation.isPending}
                      >
                        취소
                      </Button>
                    </>
                  )}
                  {reservation.status === 'CONFIRMED' && (
                    <Button variant="outline" size="sm">
                      티켓 보기
                    </Button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyReservationsPage;
