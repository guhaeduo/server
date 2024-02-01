package org.inflearngg.match.service.processing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.service.RiotAPIMatchInfo;

import java.util.ArrayList;
import java.util.List;

import static org.inflearngg.match.service.processing.ProcessingRankInfo.SummonerRankInfo.setSummonerRankAndLane;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class ProcessingMatchInfo {


    public static void setProcessingInfoData(RiotAPIMatchInfo.MatchBasicInfo riotApiData, ProcessingMatchInfo.Info.MaxDamage maxDamage, ProcessingMatchInfo.MatchData processingData) {
        ProcessingMatchInfo.Info info = new ProcessingMatchInfo.Info();
        Info.setMatchGameInfo(info, riotApiData, maxDamage);
        processingData.setInfo(info);
        processingData.setInfo(info);
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class MatchData {
        private Info info;
        private ParticipantInfo currentSummonerRankInfo;
        private Team red;
        private Team blue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Info {
        private int gameDuration;
        private String gameEndStamp;
        private int queueType; // 게임 유형 -> 나중에 솔로겜인지, 자유랭인지
        private boolean quickShutdown; // 다시 하기 유무
        private MaxDamage maxDamage;

        private static void setMatchGameInfo(ProcessingMatchInfo.Info info, RiotAPIMatchInfo.MatchBasicInfo matchData, ProcessingMatchInfo.Info.MaxDamage maxDamage) {
            info.setGameDuration(Integer.parseInt(matchData.getGameDuration()));
            info.setGameEndStamp(matchData.getGameEndTimestamp());
            info.setQueueType(matchData.getQueueId());
            //다시하기 유무

            info.setMaxDamage(maxDamage);
        }

        public static void setProcessingTeamAndCurrentSummonerAndMaxDamageData(int queueType, RiotAPIMatchInfo.ParticipantInfo[] participants, ProcessingMatchInfo.Info.MaxDamage maxDamage, ProcessingMatchInfo.Team redTeam, ProcessingMatchInfo.Team blueTeam, ProcessingMatchInfo.MatchData matchData, ProcessingRankInfo.SummonerRankInfo summonerRankInfo, String curSummonerPuuid) {
            ProcessingMatchInfo.ParticipantInfo participant;
            int redTotalKill = 0;
            int redTotalDeath = 0;
            int redTotalAssists = 0;
            int redTotalGold = 0;
            int redMaxDamage = 0;
            int blueTotalKill = 0;
            int blueTotalDeath = 0;
            int blueTotalAssists = 0;
            int blueTotalGold = 0;
            int blueMaxDamage = 0;
            blueTeam.setParticipants(new ArrayList<>());
            redTeam.setParticipants(new ArrayList<>());

            for (RiotAPIMatchInfo.ParticipantInfo p : participants) {
                participant = ProcessingMatchInfo.ParticipantInfo.setProcessingParticipantInfo(p);
                if (p.getPuuid().equals(curSummonerPuuid)) {
                    matchData.setCurrentSummonerRankInfo(participant);
                    // 참여자 데이터 가공하기
                    setSummonerRankAndLane(p, summonerRankInfo, queueType);
                    log.info("summonerRankInfo : " + summonerRankInfo);
                }
                int teamId = p.getTeamId();
                if (teamId == 100) {// 블루팀
                    //블루팀 추가
                    blueTeam.getParticipants().add(participant);
                    blueTotalKill += p.getKills();
                    blueTotalDeath += p.getDeaths();
                    blueTotalAssists += p.getAssists();
                    blueTotalGold += p.getGoldEarned();
                    blueMaxDamage = Math.max(blueMaxDamage, p.getTotalDamageDealtToChampions());
                } else {// 레드팀
                    //레드팀 추가
                    redTeam.getParticipants().add(participant);
                    redTotalKill += p.getKills();
                    redTotalDeath += p.getDeaths();
                    redTotalAssists += p.getAssists();
                    redTotalGold += p.getGoldEarned();
                    redMaxDamage = Math.max(redMaxDamage, p.getTotalDamageDealtToChampions());
                }
                if (maxDamage.getDamage() < p.getTotalDamageDealtToChampions()) {
                    maxDamage.setDamage(p.getTotalDamageDealtToChampions());
                    maxDamage.setRiotGameTag(p.getRiotIdTagline());
                    maxDamage.setRiotGameName(p.getRiotIdGameName());
                    maxDamage.setChampionName(p.getChampionName());
                    maxDamage.setChampionIconNumber(p.getChampionId());
                }
            }

            ProcessingMatchInfo.Team.setTeamData(redTeam, redTotalKill, redTotalDeath, redTotalAssists, redTotalGold, redMaxDamage);
            ProcessingMatchInfo.Team.setTeamData(blueTeam, blueTotalKill, blueTotalDeath, blueTotalAssists, blueTotalGold, blueMaxDamage);
        }
        @Getter
        @Setter
        @NoArgsConstructor
        public static class MaxDamage {
            private int damage;
            private String riotGameName;
            private String riotGameTag;
            private String championName;
            private int championIconNumber;

            // getters and setters
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Team {
        private int totalKills;
        private int totalDeaths;
        private int totalAssists;
        private int totalGold;
        private int teamMaxDamage;
        private Objectives objectives;
        private List<ParticipantInfo> participants;
        private boolean isWin;

        //nullable
        private Integer grade;

        // getters and setters
        public static void setTeamData(ProcessingMatchInfo.Team redTeam, int redTotalKill, int redTotalDeath, int redTotalAssists, int redTotalGold, int redMaxDamage) {
            redTeam.setTotalKills(redTotalKill);
            redTeam.setTotalDeaths(redTotalDeath);
            redTeam.setTotalAssists(redTotalAssists);
            redTeam.setTotalGold(redTotalGold);
            redTeam.setTeamMaxDamage(redMaxDamage);
        }

        public static void setObjectiveAndIsWinData(RiotAPIMatchInfo.Team[] teams, ProcessingMatchInfo.Team redTeam, ProcessingMatchInfo.Team blueTeam) {
            for (int i = 0; i < teams.length; i++) {
                if (teams[i].getTeamId() == 100) { //blueTeam
                    Objectives.setObjectives(teams[i], blueTeam);
                    blueTeam.setWin(teams[i].isWin());
                } else { // 200 redTeam
                    Objectives.setObjectives(teams[i], redTeam);
                    redTeam.setWin(teams[i].isWin());
                }
            }
        }
    }

    // 가공 해야됨.
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Objectives {
        private int baron;
        private int dragon;
        private int horde;
        private int inhibitor;
        private int riftHerald;
        private int tower;

        // getters and setters
        private static void setObjectives(RiotAPIMatchInfo.Team teams, ProcessingMatchInfo.Team team) {
            team.setObjectives(new ProcessingMatchInfo.Objectives());
            team.getObjectives().setBaron(teams.getObjectives().getBaron().getKills());
            team.getObjectives().setDragon(teams.getObjectives().getDragon().getKills());
            team.getObjectives().setHorde(teams.getObjectives().getRiftHerald().getKills());
            team.getObjectives().setInhibitor(teams.getObjectives().getInhibitor().getKills());
            team.getObjectives().setRiftHerald(teams.getObjectives().getRiftHerald().getKills());
            team.getObjectives().setTower(teams.getObjectives().getTower().getKills());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ParticipantInfo {
        // 라인이 있으면 좋을 듯
        private String lane;
        private int kill;
        private boolean isBot;
        private int death;
        private int assist;
        private int minionKill;
        private String championName;
        private int championIconNumber;
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


        public static ProcessingMatchInfo.ParticipantInfo setProcessingParticipantInfo(RiotAPIMatchInfo.ParticipantInfo p) {
            ProcessingMatchInfo.ParticipantInfo participantInfo = new ProcessingMatchInfo.ParticipantInfo();
            participantInfo.setLane(p.getTeamPosition());
            participantInfo.setKill(p.getKills());
            participantInfo.setDeath(p.getDeaths());
            participantInfo.setAssist(p.getAssists());
            participantInfo.setMinionKill(p.getTotalMinionsKilled());
            participantInfo.setChampionName(p.getChampionName());
            participantInfo.setChampionIconNumber(p.getChampionId());
            participantInfo.setChampionLevel(p.getChampLevel());
            participantInfo.setRiotName(p.getRiotIdGameName());
            participantInfo.setRiotTag(p.getRiotIdTagline());
            participantInfo.setVisionScore(p.getVisionScore());
            participantInfo.setTotalDamage(p.getTotalDamageDealtToChampions());
            participantInfo.setTotalGold(p.getGoldEarned());
            participantInfo.setItemNumberList(
                    new ArrayList<>() {
                        {
                            add(p.getItem0());
                            add(p.getItem1());
                            add(p.getItem2());
                            add(p.getItem3());
                            add(p.getItem4());
                            add(p.getItem5());
                            add(p.getItem6());
                        }
                    }
            );
            Perks.setPerks(p, participantInfo);
            participantInfo.setVisionWards(p.getVisionWardsBoughtInGame());
            participantInfo.setVisionScore(p.getVisionScore());
            participantInfo.setWardPlaced(p.getWardsPlaced());
            participantInfo.setSpell1Id(p.getSummoner1Id());
            participantInfo.setSpell2Id(p.getSummoner2Id());
            participantInfo.setPUuid(p.getPuuid());

            return participantInfo;
        }
        public void setLane(String lane) {
            this.lane = lane;
            if (lane.equals("UTILITY")) {
                this.lane = "SUPPORT";
            }
        }


    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Perks {
        private PerkDetail main;
        private PerkDetail sub;

        // getters and setters
        private static void setPerks(RiotAPIMatchInfo.ParticipantInfo p, ProcessingMatchInfo.ParticipantInfo participantInfo) {
            // 초기화
            ProcessingMatchInfo.PerkDetail mainPerk = new ProcessingMatchInfo.PerkDetail(0, new ArrayList<>());
            ProcessingMatchInfo.PerkDetail subPerk = new ProcessingMatchInfo.PerkDetail(0, new ArrayList<>());
            participantInfo.setPerks(new ProcessingMatchInfo.Perks(mainPerk, subPerk));

            for (RiotAPIMatchInfo.ParticipantInfo.Perks.Style style : p.getPerks().getStyles()) {
                if (style.getDescription().equals("primaryStyle")) {
                    PerkDetail.setPerksDetail(mainPerk, style);
                }
                if (style.getDescription().equals("subStyle")) {
                    PerkDetail.setPerksDetail(subPerk, style);
                }
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PerkDetail {
        private int perkStyle;
        private ArrayList<Integer> perkList;
        // getters and setters
        private static void setPerksDetail(ProcessingMatchInfo.PerkDetail perksDetail, RiotAPIMatchInfo.ParticipantInfo.Perks.Style style) {
            for (RiotAPIMatchInfo.ParticipantInfo.Perks.Style.Selections selection : style.getSelections()) {
                perksDetail.getPerkList().add(selection.getPerk());
            }
            perksDetail.setPerkStyle(style.getStyle());
        }
    }


}
