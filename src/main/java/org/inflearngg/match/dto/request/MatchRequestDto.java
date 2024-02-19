package org.inflearngg.match.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MatchRequestDto {


    @Getter
    public static enum QueueType {
        ALL(0),
        NORMAL(490),
        SOLO(420),
        FREE(440);

        private final int queueId;

        QueueType(int queueId) {
            this.queueId = queueId;
        }

    }

}
