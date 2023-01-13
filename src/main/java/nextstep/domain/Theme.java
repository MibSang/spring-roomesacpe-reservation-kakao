package nextstep.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.web.theme.dto.CreateThemeRequestDto;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@AllArgsConstructor
public class Theme {

    private Long id;
    private String name;
    private String desc;
    private Integer price;

    private Theme(Long id) {
        this.id = id;
    }

    private Theme(String name, String desc, Integer price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public static Theme of(String name, String desc, Integer price) {
        return new Theme(name, desc, price);
    }

    public static Theme of(Long id, String name, String desc, Integer price) {
        return new Theme(id, name, desc, price);
    }

    public static Theme of(CreateThemeRequestDto requestDto, Long id) {
        return Theme.builder()
                .id(id)
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .price(requestDto.getPrice())
                .build();
    }

    public static Theme from(ResultSet rs) throws SQLException {
        return Theme.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .desc(rs.getString("desc"))
                .price(rs.getInt("price"))
                .build();
    }

    public static Theme from(CreateThemeRequestDto requestDto) {
        return Theme.builder()
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .price(requestDto.getPrice())
                .build();
    }

    public static Theme from(Long id) {
        return new Theme(id);
    }
}
