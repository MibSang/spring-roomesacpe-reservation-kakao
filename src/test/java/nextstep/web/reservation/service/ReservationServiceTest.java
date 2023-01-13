package nextstep.web.reservation.service;

import nextstep.domain.Reservation;
import nextstep.domain.Theme;
import nextstep.web.reservation.dto.CreateReservationRequestDto;
import nextstep.web.reservation.repository.ReservationDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationDao reservationDao;

    @InjectMocks
    ReservationService reservationService;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(1L)
                .date(LocalDate.of(2023, 1, 10))
                .time(LocalTime.MIDNIGHT)
                .name("베인")
                .theme(Theme.of(1L, "test", "testetsete", 2000))
                .build();
    }

    @Test
    void 예약을_생성할_수_있다() {
        CreateReservationRequestDto requestDto = new CreateReservationRequestDto(
                LocalDate.of(2023, 1, 10), LocalTime.of(13, 0), "reservation1", 1L
        );
        when(reservationDao.save(any()))
                .thenReturn(1L);

        Assertions.assertThat(reservationService.createReservation(requestDto).getLocation())
                .isEqualTo("/reservations/1");
    }

    @Test
    void 예약을_조회할_수_있다() {
        Long id = 1L;

        when(reservationDao.findByIdThemeJoined(anyLong()))
                .thenReturn(Optional.ofNullable(reservation));

        Assertions.assertThat(reservationService.findReservation(id).getId())
                .isEqualTo(id);
        verify(reservationDao, times(1)).findByIdThemeJoined(id);
    }

    @Test
    void 예약을_취소할_수_있다() {
        Long id = 1L;
        doNothing().when(reservationDao)
                .deleteById(anyLong());

        Assertions.assertThatNoException()
                .isThrownBy(() -> reservationService.deleteReservation(id));
        verify(reservationDao, times(1)).deleteById(id);
    }
}
