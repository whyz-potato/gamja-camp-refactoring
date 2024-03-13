package whyzpotato.gamjacamp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    @Getter
    @NoArgsConstructor
    public static class MultipleChoices<T> {
        private String messages;
        private ArrayList<T> choices;

        @Builder
        public MultipleChoices(String messages, List<T> choices) {
            this.messages = messages;
            this.choices = new ArrayList<T>();
            this.choices.addAll(choices);
        }

        public void addChoice(T choice) {
            choices.add(choice);
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Choice {
        private String description;
        private URI url;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResult<T> {
        private int numberOfElements;
        private long totalElements;
        private int totalPages;
        private int page;
        private int size;
        private long offset;
        private Boolean isFirst;
        private Boolean isLast;
        private List<T> content;

        public PageResult(Page<T> page) {
            this.numberOfElements = page.getNumberOfElements();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.page = page.getPageable().getPageNumber();
            this.size = page.getPageable().getPageSize();
            this.offset = page.getPageable().getOffset();
            this.isFirst = page.isFirst();
            this.isLast = page.isLast();
            this.content = page.getContent();
        }
    }


}
