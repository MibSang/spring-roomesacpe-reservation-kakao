package nextstep.web.theme.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.Theme;
import nextstep.web.common.exception.BusinessException;
import nextstep.web.common.exception.CommonErrorCode;
import nextstep.web.reservation.repository.ReservationDao;
import nextstep.web.theme.dto.*;
import nextstep.web.theme.repository.ThemeDao;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;


    public CreateThemeResponseDto createTheme(CreateThemeRequestDto requestDto) {
        Theme theme = Theme.from(requestDto);

        return CreateThemeResponseDto.from(themeDao.save(theme));
    }

    public FindThemeResponseDto findTheme(Long id) {
        Theme theme = themeDao.findById(id);

        return FindThemeResponseDto.of(theme);
    }

    public FindAllThemeResponseDto findAllTheme() {
        return new FindAllThemeResponseDto(themeDao.findAll());
    }

    public void deleteTheme(Long id) {
        if (isReserved(id)) {
            throw new BusinessException(CommonErrorCode.RESERVED_THEME_ERROR);
        }
        themeDao.deleteById(id);
    }

    public void updateTheme(CreateThemeRequestDto requestDto, Long id) {
        if (isReserved(id)) {
            throw new BusinessException(CommonErrorCode.RESERVED_THEME_ERROR);
        }
        if (themeDao.update(Theme.of(requestDto, id)) == 0) {
            throw new BusinessException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    private boolean isReserved(Long themeId) {
        return reservationDao.findByThemeId(themeId).size() != 0;
    }
}
