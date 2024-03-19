package org.inflearngg.duo.dto;

public enum Lane {

    TOP("탑"), JUNGLE("정글"), MID("미드"), BOT("원딜"), SUP("서폿");

    private String description;

    Lane(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
