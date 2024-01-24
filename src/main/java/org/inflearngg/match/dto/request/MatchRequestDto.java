package org.inflearngg.match.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MatchRequestDto {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class QueueType {
        private String queueType;
    }

}
