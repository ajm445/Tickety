import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Clock, AlertCircle } from 'lucide-react';
import { Button, Loading, Modal } from '../components/common';
import { useSeats, useCreateReservation } from '../features/reservation';
import type { Seat, SeatStatus } from '../types';

const seatStatusColors: Record<SeatStatus, string> = {
  AVAILABLE: 'bg-green-500 hover:bg-green-600 cursor-pointer',
  RESERVED: 'bg-yellow-500 cursor-not-allowed',
  SOLD: 'bg-gray-400 cursor-not-allowed',
};

const gradeColors = {
  VIP: 'border-purple-500',
  R: 'border-red-500',
  S: 'border-blue-500',
  A: 'border-green-500',
};

export const ReservationPage = () => {
  const { concertId } = useParams<{ concertId: string }>();
  const navigate = useNavigate();
  const [selectedSeats, setSelectedSeats] = useState<Seat[]>([]);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const { data: seats, isLoading, error } = useSeats(Number(concertId));
  const createReservation = useCreateReservation();

  const handleSeatClick = (seat: Seat) => {
    if (seat.status !== 'AVAILABLE') return;

    setSelectedSeats((prev) => {
      const isSelected = prev.some((s) => s.seatId === seat.seatId);
      if (isSelected) {
        return prev.filter((s) => s.seatId !== seat.seatId);
      }
      return [...prev, seat];
    });
  };

  const handleReserve = async () => {
    if (selectedSeats.length === 0) return;

    try {
      await createReservation.mutateAsync({
        concertId: Number(concertId),
        seatIds: selectedSeats.map((s) => s.seatId),
      });
      navigate('/reservations');
    } catch (err) {
      console.error('Reservation failed:', err);
    }
  };

  const totalPrice = selectedSeats.reduce((sum, seat) => sum + seat.price, 0);

  if (isLoading) {
    return <Loading fullScreen text="좌석 정보를 불러오는 중..." />;
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[50vh]">
        <AlertCircle className="h-12 w-12 text-red-500 mb-4" />
        <p className="text-gray-600">좌석 정보를 불러오는데 실패했습니다. 다시 시도해주세요.</p>
      </div>
    );
  }

  // Group seats by row
  const seatsByRow = seats?.reduce((acc, seat) => {
    const row = seat.seatNumber.split('-')[0];
    if (!acc[row]) acc[row] = [];
    acc[row].push(seat);
    return acc;
  }, {} as Record<string, Seat[]>) || {};

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      {/* Header */}
      <div className="mb-8">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-4"
        >
          <ArrowLeft className="h-4 w-4" />
          뒤로가기
        </button>
        <h1 className="text-3xl font-bold text-gray-900">좌석 선택</h1>
        <p className="mt-2 text-gray-600">
          원하는 좌석을 선택해주세요
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Seat Map */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            {/* Stage */}
            <div className="bg-gray-200 rounded-lg py-4 text-center text-gray-600 font-medium mb-8">
              무대
            </div>

            {/* Seats */}
            <div className="space-y-4">
              {Object.entries(seatsByRow).map(([row, rowSeats]) => (
                <div key={row} className="flex items-center gap-2">
                  <span className="w-8 text-sm font-medium text-gray-500">{row}</span>
                  <div className="flex gap-2 flex-wrap">
                    {rowSeats.map((seat) => {
                      const isSelected = selectedSeats.some((s) => s.seatId === seat.seatId);
                      return (
                        <button
                          key={seat.seatId}
                          onClick={() => handleSeatClick(seat)}
                          disabled={seat.status !== 'AVAILABLE'}
                          className={`
                            w-10 h-10 rounded-lg text-xs font-medium text-white
                            border-2 transition-all
                            ${seatStatusColors[seat.status]}
                            ${gradeColors[seat.seatGrade]}
                            ${isSelected ? 'ring-2 ring-offset-2 ring-blue-500' : ''}
                          `}
                          title={`${seat.seatNumber} - ${seat.seatGrade} - ${seat.price.toLocaleString()}원`}
                        >
                          {seat.seatNumber.split('-')[1]}
                        </button>
                      );
                    })}
                  </div>
                </div>
              ))}
            </div>

            {/* Legend */}
            <div className="mt-8 flex flex-wrap gap-4 text-sm text-gray-600">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-green-500" />
                <span>예약 가능</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-yellow-500" />
                <span>예약 중</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-gray-400" />
                <span>판매 완료</span>
              </div>
            </div>
          </div>
        </div>

        {/* Selection Summary */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-xl p-6 shadow-sm sticky top-24 border border-gray-100">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              선택한 좌석
            </h2>

            {selectedSeats.length === 0 ? (
              <p className="text-gray-500 text-sm">선택된 좌석이 없습니다</p>
            ) : (
              <div className="space-y-3">
                {selectedSeats.map((seat) => (
                  <div
                    key={seat.seatId}
                    className="flex justify-between items-center py-2 border-b"
                  >
                    <div>
                      <span className="font-medium text-gray-900">{seat.seatNumber}</span>
                      <span className="ml-2 text-sm text-gray-500">({seat.seatGrade})</span>
                    </div>
                    <span className="text-gray-900">{seat.price.toLocaleString()}원</span>
                  </div>
                ))}
              </div>
            )}

            <div className="mt-6 pt-4 border-t">
              <div className="flex justify-between items-center mb-4">
                <span className="font-semibold text-gray-900">합계</span>
                <span className="text-xl font-bold text-blue-600">
                  {totalPrice.toLocaleString()}원
                </span>
              </div>

              <div className="flex items-center gap-2 text-sm text-yellow-600 mb-4">
                <Clock className="h-4 w-4" />
                <span>예약 후 10분 내 결제 필요</span>
              </div>

              <Button
                className="w-full"
                disabled={selectedSeats.length === 0}
                isLoading={createReservation.isPending}
                onClick={() => setShowConfirmModal(true)}
              >
                {selectedSeats.length}석 예약하기
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Confirm Modal */}
      <Modal
        isOpen={showConfirmModal}
        onClose={() => setShowConfirmModal(false)}
        title="예약 확인"
      >
        <div className="space-y-4">
          <p className="text-gray-600">다음 좌석을 예약하시겠습니까?</p>
          <div className="bg-gray-50 rounded-lg p-4">
            <div className="space-y-2">
              {selectedSeats.map((seat) => (
                <div key={seat.seatId} className="flex justify-between text-sm text-gray-900">
                  <span>{seat.seatNumber} ({seat.seatGrade})</span>
                  <span>{seat.price.toLocaleString()}원</span>
                </div>
              ))}
            </div>
            <div className="mt-4 pt-4 border-t flex justify-between font-semibold text-gray-900">
              <span>합계</span>
              <span>{totalPrice.toLocaleString()}원</span>
            </div>
          </div>
          <div className="flex gap-3 justify-end">
            <Button variant="outline" onClick={() => setShowConfirmModal(false)}>
              취소
            </Button>
            <Button
              onClick={handleReserve}
              isLoading={createReservation.isPending}
            >
              예약 확정
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default ReservationPage;
