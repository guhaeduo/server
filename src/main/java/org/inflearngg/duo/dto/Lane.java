package org.inflearngg.duo.dto;

import java.util.Arrays;

public enum Lane {
    ALL, TOP, MID, ADC, SUP, JUG;

    public static Lane findByDescription(String description, String name) {
        return Arrays.stream(Lane.values())
                .filter(lane -> lane.name().equals(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 라인이 없습니다." + name + " = " + description));
    }
    public static String InputValueLaneCheck(String inputLane) {
        String lane = inputLane.toUpperCase();
        if(lane.equals("ALL")) {
            return null;
        }
        return Lane.valueOf(inputLane).name();
    }

//    TOP("탑"), JUNGLE("정글"), MID("미드"), BOT("원딜"), SUP("서폿");
//
//    private String description;
//
//    Lane(String description) {
//        this.description = description;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public static Lane findByDescription(String description) {
//        return Arrays.stream(Lane.values())
//                .filter(lane -> lane.getDescription().equals(description))
//                .findAny()
//                .orElseThrow(() -> new IllegalArgumentException("해당하는 라인이 없습니다. description=" + description));
//    }
}
