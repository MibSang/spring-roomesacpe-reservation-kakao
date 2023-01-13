package nextstep.web.reservation.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.Reservation;
import nextstep.web.common.exception.BusinessException;
import nextstep.web.common.exception.CommonErrorCode;
import nextstep.web.reservation.dto.CreateReservationRequestDto;
import nextstep.web.reservation.dto.CreateReservationResponseDto;
import nextstep.web.reservation.dto.FindReservationResponseDto;
import nextstep.web.reservation.repository.ReservationDao;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public CreateReservationResponseDto createReservation(CreateReservationRequestDto requestDto) {
        Reservation reservation = Reservation.from(requestDto);

        return CreateReservationResponseDto.from(reservationDao.save(reservation));
    }

    public FindReservationResponseDto findReservation(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.RESOURCE_NOT_FOUND));

        return FindReservationResponseDto.of(reservation);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
