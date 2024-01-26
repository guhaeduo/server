package org.inflearngg.match.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.inflearngg.match.dto.response.MatchingResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ProcessingMatchInfo {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MatchData {
        private Info info;
        private ParticipantInfo currentSummonerMatchData;
        private Team red;
        private Team blue;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Info{
        private int gameDuration;
        private int gameEndStamp;
        private String queueType; // 게임 유형
        private boolean quickShutdown; // 다시 하기 유무
        private MaxDamage maxDamage;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Team {
        private int totalKill;
        private int totalDeath;
        private int totalAssists;
        private int totalGold;
        private int teamMaxDamage;
        private Objectives objectives;
        private List<ParticipantInfo> participants;
        private boolean isWin;

        //nullable
        private Integer grade;

        // getters and setters

    }
    // 가공 해야됨.
    @Getter
    @Setter
    @NoArgsConstructor
    protected static class Objectives {
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
    @NoArgsConstructor
    protected static class ParticipantInfo {
        // 라인이 있으면 좋을 듯
        private String lane;
        private int kill;
        private boolean isBot;
        private int death;
        private int assist;
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
        private String pUuid;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class Perks {
        private PerkDetail main;
        private PerkDetail sub;

        // getters and setters
    }
    @Getter
    @Setter
    @AllArgsConstructor
    protected static class PerkDetail {
        private int perkStyle;
        private ArrayList<Integer> perkList;
        // getters and setters
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class MaxDamage {
        private int damage;
        private String riotGameName;
        private String riotGameTag;
        private String championName;

        // getters and setters
    }
}
