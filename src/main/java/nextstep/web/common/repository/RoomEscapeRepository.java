package nextstep.web.common.repository;

import java.util.List;
import java.util.Optional;

public interface RoomEscapeRepository<T> {

    Optional<T> findById(Long id);

    Long save(T entity);

    void deleteById(Long id);

    List<T> findAll();
}
