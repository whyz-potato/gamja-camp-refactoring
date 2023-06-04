package whyzpotato.gamjacamp.dto.Post;

import lombok.Builder;

import java.util.List;

public class SimpleGeneralPostListResponseDto {
    int total;
    int start;
    int display;
    List<SimpleGeneralPostListResponseDto> posts;

    @Builder
    public SimpleGeneralPostListResponseDto(int total, int start, int display, List<SimpleGeneralPostListResponseDto> posts) {
        this.total = total;
        this.start = start;
        this.display = display;
        this.posts = posts;
    }
}
