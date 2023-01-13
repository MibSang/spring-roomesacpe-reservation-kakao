package nextstep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.web.reservation.dto.CreateReservationRequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@AllArgsConstructor
public class Reservation {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String name;
    private Theme theme;

    private Reservation(LocalDate date, LocalTime time, String name, Theme theme) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.theme = theme;
    }

    public static Reservation of(LocalDate date, LocalTime time, String name, Theme theme) {
        return new Reservation(date, time, name, theme);
    }

    public static Reservation from(ResultSet rs) throws SQLException {
        try {
            return Reservation.builder()
                    .id(rs.getLong("id"))
                    .date(rs.getDate("date").toLocalDate())
                    .time(rs.getTime("time").toLocalTime())
                    .name(rs.getString("name"))
                    .theme(Theme.of(rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("theme_desc"),
                            rs.getInt("theme_price")))
                    .build();
        } catch (SQLException e) {
            return Reservation.builder()
                    .id(rs.getLong("id"))
                    .date(rs.getDate("date").toLocalDate())
                    .time(rs.getTime("time").toLocalTime())
                    .name(rs.getString("name"))
                    .theme(Theme.from(rs.getLong("theme_id")))
                    .build();
        }
    }

    public static Reservation from(CreateReservationRequestDto requestDto){
        return Reservation.builder()
                .date(requestDto.getDate())
                .time(requestDto.getTime())
                .name(requestDto.getName())
                .theme(Theme.from(requestDto.getThemeId()))
                .build();
    }
}
