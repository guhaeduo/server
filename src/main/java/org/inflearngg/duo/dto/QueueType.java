package org.inflearngg.duo.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public enum QueueType {
    ALL, SOLO, FREE, NORMAL, ARAM;

    public static QueueType findByDescription(String description, String name) {
        return Arrays.stream(QueueType.values())
                .filter(queueType -> queueType.name().equals(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 큐 타입이 없습니다." + name + " = " + description));
    }

    public static String InputValueQueueTypeCheck(String inputQueueType) {
        String queueType = inputQueueType.toUpperCase();
        if (queueType.equals("ALL")) {
            return null;
        }
        log.info("ENUM queueType : " + QueueType.valueOf(queueType).name());
        return QueueType.valueOf(queueType).name();
    }

//    ALL("모든 큐"), SOLO("솔로랭크"), FREE("자유랭크"), NORMAL("일반"), ABYSS("칼바람");

//    private String description;
//    QueueType(String description) {
//        this.description = description;

//    }
//    public String getDescription() {
//        return description;
//    }
//    public static QueueType findByDescription(String description) {
//        return Arrays.stream(QueueType.values())
//                .filter(queueType -> queueType.getDescription().equals(description))
//                .findAny()
//                .orElseThrow(() -> new IllegalArgumentException("해당하는 큐 타입이 없습니다. description=" + description));
//    }
}
