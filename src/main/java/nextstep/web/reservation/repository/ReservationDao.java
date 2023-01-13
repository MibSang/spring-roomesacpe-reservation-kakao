package nextstep.web.reservation.repository;

import lombok.RequiredArgsConstructor;
import nextstep.domain.Reservation;
import nextstep.web.common.exception.BusinessException;
import nextstep.web.common.exception.CommonErrorCode;
import nextstep.web.common.repository.RoomEscapeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationDao implements RoomEscapeRepository<Reservation> {

    public static final String TABLE_NAME = "reservation";
    public static final String KEY_COLUMN_NAME = "id";
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> Reservation.from(resultSet);

    public Long save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);

        Number key = simpleJdbcInsert.executeAndReturnKey(prepareParams(reservation));

        return key.longValue();
    }


    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM RESERVATION WHERE id = ?;";
        Reservation reservation;
        try {
            reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            reservation = null;
        }

        return Optional.ofNullable(reservation);
    }

    public Optional<Reservation> findByIdThemeJoined(Long id) {
        String sql = "SELECT R.id AS id, R.date AS date, R.time AS time, R.name AS name, T.id AS theme_id, T.name AS theme_name, T.desc AS theme_desc, T.price AS theme_price " +
                "FROM RESERVATION AS R " +
                "JOIN THEME AS T " +
                "ON R.theme_id = T.id " +
                "WHERE R.id = ?;";
        Reservation reservation;
        try {
            reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            reservation = null;
        }

        return Optional.ofNullable(reservation);
    }

    public List<Reservation> findByThemeId(Long themeId) {
        String sql = "SELECT * FROM RESERVATION WHERE theme_id = ?;";
        return jdbcTemplate.query(sql, reservationRowMapper, themeId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM RESERVATION WHERE ID = ?;";
        if (jdbcTemplate.update(sql, id) == 0) {
            throw new BusinessException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    public List<Reservation> findAll() {
        String sql = "SELECT * FROM THEME;";

        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    private Map<String, Object> prepareParams(Reservation reservation) {
        return Map.of(
                "date", reservation.getDate(),
                "time", reservation.getTime(),
                "name", reservation.getName(),
                "theme_id", reservation.getTheme().getId()
        );
    }

}
