package whyzpotato.gamjacamp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
