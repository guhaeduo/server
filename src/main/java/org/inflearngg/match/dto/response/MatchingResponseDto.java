package org.inflearngg.match.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MatchingResponseDto {
    @Getter
    @Setter
    public static class MatchData {
        private Info info;
        private SummonerMatchData currentSummonerMatchData;
        private Team red;
        private Team blue;
    }
    @Getter
    @Setter
    public class Info {
        private int gameDuration;
        private int gameEndStamp;
        private String queueType;
        private boolean quickShutdown;
        private int maxDamage;

        // getters and setters
    }
    @Getter
    @Setter
    public static class Team {
        private int totalKill;
        private int totalDeath;
        private int totalAssists;
        private int totalGold;
        private Objectives objectives;
        private List<SummonerMatchData> participants;
        private boolean isWin;

        //nullable
        private Integer grade;

        // getters and setters
    }
    @Getter
    @Setter
    public class Objectives {
        private int baron;
        private int dragon;
        private int horde;
        private int inhibitor;
        private int riftHerald;
        private int tower;

        // getters and setters
    }
    @Getter
    @Setter
    public static class SummonerMatchData {
        private int kill;
        private boolean isBot;
        private int death;
        private int assists;
        private int damage;
        private int minionKill;
        private String championName;
        private int championLevel;
        private String riotName;
        private String riotTag;
        private int totalDamage;
        private int totalGold;
        private List<Integer> itemNumberList;
        private Perks perks;
        private int visionWards;
        private int visionScore;
        private int wardPlaced;
        private int spell1Id;
        private int spell2Id;
        private int pUuid;

        // getters and setters
    }

    @Getter
    @Setter
    public static class Perks {
        private Object main;
        private Object sub;

        // getters and setters
    }


}
