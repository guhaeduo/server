package org.inflearngg.match.mapper;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.dto.MaxStat;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.inflearngg.match.dto.process.ProcessRankInfo;
import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.inflearngg.match.dto.response.MatchingResponseDto.MatchData.MatchInfo.MaxData.*;

@Slf4j
@Component
public class ProcessMatchMapper {


    /**
     * RankSummary
     */
    public MatchingResponseDto.SummonerRankInfo mapToSummonerRankInfo(ProcessRankInfo processRankInfo) {
        log.info("가공데이터 매핑전 현재 시간: " + System.currentTimeMillis());
        if (processRankInfo == null) return new MatchingResponseDto.SummonerRankInfo();
        MatchingResponseDto.SummonerRankInfo matchingSummonerRankInfo = new MatchingResponseDto.SummonerRankInfo();
        matchingSummonerRankInfo.setInfo(mapToRankInfo(processRankInfo.getInfo()));
        matchingSummonerRankInfo.setLane(mapToLane(processRankInfo.getLane()));
        log.info("가공데이터 매핑후 현재 시간: " + System.currentTimeMillis());
        return matchingSummonerRankInfo;
    }

    private MatchingResponseDto.SummonerRankInfo.RankInfo mapToRankInfo(ProcessRankInfo.RankInfo rankInfo) {
        MatchingResponseDto.SummonerRankInfo.RankInfo matchingRankInfo = new MatchingResponseDto.SummonerRankInfo.RankInfo();
        if (rankInfo.getCntGame() == 0) {
            matchingRankInfo.setCntGame(0);
            matchingRankInfo.setWinningRate(0.0);
            matchingRankInfo.setWins(0);
            matchingRankInfo.setLoses(0);
            matchingRankInfo.setKda(0.0);
            matchingRankInfo.setKillAvg(0);
            matchingRankInfo.setDeathAvg(0);
            matchingRankInfo.setAssistAvg(0);
            matchingRankInfo.setMainLane("");
            matchingRankInfo.setSubLane("");
            return matchingRankInfo;
        }
        matchingRankInfo.setCntGame(rankInfo.getCntGame());
        matchingRankInfo.setWinningRate((double) rankInfo.getWins() / rankInfo.getCntGame() * 100);
        matchingRankInfo.setWins(rankInfo.getWins());
        matchingRankInfo.setLoses(rankInfo.getLoses());
        matchingRankInfo.setKda((rankInfo.getTotalKills() + rankInfo.getTotalAssists()) / (double) rankInfo.getTotalDeaths());
        if (rankInfo.getCntGame() == 0) {
            matchingRankInfo.setKillAvg(0);
            matchingRankInfo.setDeathAvg(0);
            matchingRankInfo.setAssistAvg(0);
            return matchingRankInfo;
        }
        matchingRankInfo.setKillAvg(rankInfo.getTotalKills() / rankInfo.getCntGame());
        matchingRankInfo.setDeathAvg(rankInfo.getTotalDeaths() / rankInfo.getCntGame());
        matchingRankInfo.setAssistAvg(rankInfo.getTotalAssists() / rankInfo.getCntGame());
        matchingRankInfo.setMainLane(rankInfo.getMainLane());
        matchingRankInfo.setSubLane(rankInfo.getSubLane());

        return matchingRankInfo;
    }

    private MatchingResponseDto.SummonerRankInfo.Lane mapToLane(ProcessRankInfo.Lane lane) {
        MatchingResponseDto.SummonerRankInfo.Lane matchingLane = new MatchingResponseDto.SummonerRankInfo.Lane();
        matchingLane.setAll(mapToRankLaneData(lane.getAll()));
        matchingLane.setTop(mapToRankLaneData(lane.getTop()));
        matchingLane.setJug(mapToRankLaneData(lane.getJug()));
        matchingLane.setMid(mapToRankLaneData(lane.getMid()));
        matchingLane.setAdc(mapToRankLaneData(lane.getAdc()));
        matchingLane.setSup(mapToRankLaneData(lane.getSup()));
        return matchingLane;
    }

    private MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData mapToRankLaneData(ProcessRankInfo.RankLaneData rankLaneData) {
        MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData matchingRankLaneData = new MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData();
        if (rankLaneData.getTotalGameCnt() == 0) {
            matchingRankLaneData.setMostChampionlist(new ArrayList<>());
            return matchingRankLaneData;
        }
        matchingRankLaneData.setCntGame(rankLaneData.getTotalGameCnt());
        matchingRankLaneData.setWinningRate((double) rankLaneData.getWins() / rankLaneData.getTotalGameCnt() * 100);
        matchingRankLaneData.setMostChampionlist(mapToMostChampionList(
                        rankLaneData.getChampions().values().stream()
                                .sorted(Comparator.comparingInt(ProcessRankInfo.RankLaneData.ChampionData::getTotalGameCnt).reversed())
                                .limit(3)
                                .collect(Collectors.toList())
                )
        );
        return matchingRankLaneData;
    }

    private List<MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion> mapToMostChampionList(List<ProcessRankInfo.RankLaneData.ChampionData> mostChampionList) {
        if (mostChampionList.isEmpty()) return new ArrayList<>();
        List<MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion> matchingMostChampionList = new ArrayList<>();
        for (ProcessRankInfo.RankLaneData.ChampionData championData : mostChampionList) {
            MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion matchingMostChampion = new MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion();
            if (championData.getTotalGameCnt() != 0) {
                matchingMostChampion.setCntGame(championData.getTotalGameCnt());
                int gameMinuteTime = championData.getGameTime() / 60;
                matchingMostChampion.setChampionName(championData.getChampionName());
                matchingMostChampion.setWinningRate((double) championData.getWins() / championData.getTotalGameCnt() * 100);
                matchingMostChampion.setCsPerMinute((double) championData.getTotalCS() / gameMinuteTime);
                matchingMostChampion.setVisionScore(championData.getVisionScore() / championData.getTotalGameCnt());
                matchingMostChampion.setKda(championData.getKda() / championData.getTotalGameCnt());
                matchingMostChampion.setKillParticipation(championData.getTotalKillParticipation() / championData.getTotalGameCnt() * 100);
            }

            matchingMostChampionList.add(matchingMostChampion);
        }
        return matchingMostChampionList;
    }

    /**
     * =========================================================================================================================================================
     * MatchList
     * =========================================================================================================================================================
     */
    public List<MatchingResponseDto.MatchData> mapToMatchDataList(List<ProcessMatchInfo> matchDataList) {
        log.info("가공데이터 매핑전 현재 시간: " + System.currentTimeMillis());
        List<MatchingResponseDto.MatchData> matchingMatchDataList = new ArrayList<>();
        for (ProcessMatchInfo matchData : matchDataList) {
            if (matchData == null) continue;
            MatchingResponseDto.MatchData matchingMatchData = new MatchingResponseDto.MatchData();
            matchingMatchData.setMatchId(matchData.getMatchId());
            matchingMatchData.setInfo(mapToInfo(matchData.getInfo()));
            int totalKills = 0;
            if (matchData.getCurrentSummonerParticipantInfo().getTeamId() == 100)
                totalKills = matchData.getBlue().getTotalKills();
            else
                totalKills = matchData.getRed().getTotalKills();

            matchingMatchData.setCurrentSummonerMatchData(mapToSummonerMatchData(matchData.getCurrentSummonerParticipantInfo(), totalKills));
            matchingMatchData.setRed(mapToTeam(matchData.getRed()));
            matchingMatchData.setBlue(mapToTeam(matchData.getBlue()));
            matchingMatchDataList.add(matchingMatchData);
        }
        log.info("가공데이터 매핑후 현재 시간: " + System.currentTimeMillis());
        return matchingMatchDataList;
    }

    private MatchingResponseDto.MatchData.MatchInfo mapToInfo(ProcessMatchInfo.Info info) {
        MatchingResponseDto.MatchData.MatchInfo matchingInfo = new MatchingResponseDto.MatchData.MatchInfo();
        matchingInfo.setGameDuration(info.getGameDuration());
        matchingInfo.setGameEndStamp(info.getGameEndStamp());
        matchingInfo.setQueueType(info.getQueueType());
        matchingInfo.setQuickShutdown(info.isQuickShutdown());
        matchingInfo.setMaxData(mapToMaxDamage(info.getMaxData()));
        return matchingInfo;
    }

    private MatchingResponseDto.MatchData.MatchInfo.MaxData mapToMaxDamage(ProcessMatchInfo.Info.MaxData maxData) {

        MatchingResponseDto.MatchData.MatchInfo.MaxData matchingMaxData = new MatchingResponseDto.MatchData.MatchInfo.MaxData();

        //최대데미지
        MaxDamage maxDamage = setDefaultMaxValues(new MaxDamage(), maxData);
        if (maxData.getMaxDamage().getDamage() != 0) {
//            setMaxValues();
            maxDamage.setDamage(maxData.getMaxDamage().getDamage());
            maxDamage.setRiotGameName(maxData.getMaxDamage().getRiotGameName());
            maxDamage.setChampionName(maxData.getMaxDamage().getChampionName());
            maxDamage.setRiotGameTag(maxData.getMaxDamage().getRiotTag());
        }
        //최대 킬
        MaxKill maxKill = setDefaultMaxValues(new MaxKill(), maxData);
        if (maxData.getMaxKill().getKill() != 0) {
//            setMaxValues();
            maxKill.setKill(maxData.getMaxKill().getKill());
            maxKill.setRiotGameName(maxData.getMaxKill().getRiotGameName());
            maxKill.setChampionName(maxData.getMaxKill().getChampionName());
            maxKill.setRiotGameTag(maxData.getMaxKill().getRiotTag());
        }
        //최대 데스
        MaxDeath maxDeath = setDefaultMaxValues(new MaxDeath(), maxData);
        if (maxData.getMaxDeath().getDeath() != 0) {
            maxDeath.setDeath(maxData.getMaxDeath().getDeath());
            maxDeath.setRiotGameName(maxData.getMaxDeath().getRiotGameName());
            maxDeath.setChampionName(maxData.getMaxDeath().getChampionName());
            maxDeath.setRiotGameTag(maxData.getMaxDeath().getRiotTag());

        }
        //최대 어시스트
        MaxAssist maxAssist = setDefaultMaxValues(new MaxAssist(), maxData);
        if (maxData.getMaxAssist().getAssist() != 0) {
            maxAssist.setAssist(maxData.getMaxAssist().getAssist());
            maxAssist.setRiotGameName(maxData.getMaxAssist().getRiotGameName());
            maxAssist.setChampionName(maxData.getMaxAssist().getChampionName());
            maxAssist.setRiotGameTag(maxData.getMaxAssist().getRiotTag());
        }
        matchingMaxData.setMaxDamage(maxDamage);
        matchingMaxData.setMaxKill(maxKill);
        matchingMaxData.setMaxDeath(maxDeath);
        matchingMaxData.setMaxAssist(maxAssist);
        return matchingMaxData;
    }

    private <T extends MaxStat> T setDefaultMaxValues(MaxStat stat , ProcessMatchInfo.Info.MaxData defaultStat) {
        stat.setRiotGameName(defaultStat.getDefaultRiotGameName());
        stat.setChampionName(defaultStat.getDefaultChampionName());
        stat.setRiotGameTag(defaultStat.getDefaultRiotTag());
        return (T) stat;
    }
    private  void setMaxValues(MaxStat stat ,MaxStat getMaxStat) {
        stat.setRiotGameName(getMaxStat.getRiotGameName());
        stat.setChampionName(getMaxStat.getChampionName());
        stat.setRiotGameTag(getMaxStat.getRiotGameTag());
//        return (T) stat;
    }



    /**
     * Team
     */
    public MatchingResponseDto.MatchData.Team mapToTeam(ProcessMatchInfo.Team team) {
        MatchingResponseDto.MatchData.Team matchingTeam = new MatchingResponseDto.MatchData.Team();
        matchingTeam.setTotalKills(team.getTotalKills());
        matchingTeam.setTotalDeaths(team.getTotalDeaths());
        matchingTeam.setTotalAssists(team.getTotalAssists());
        matchingTeam.setTotalGold(team.getTotalGold());
        matchingTeam.setTeamMaxDamage(team.getTeamMaxDamage());
        matchingTeam.setWin(team.isWin());
        matchingTeam.setObjectives(mapToObjects(team.getObjectives()));
        matchingTeam.setParticipants(mapToParticipants(team.getParticipants(), team.getTotalKills()));
        return matchingTeam;
    }

    private MatchingResponseDto.MatchData.Team.Objectives mapToObjects(ProcessMatchInfo.Team.Objectives objectives) {
        MatchingResponseDto.MatchData.Team.Objectives matchingObjectives = new MatchingResponseDto.MatchData.Team.Objectives();
        matchingObjectives.setBaron(objectives.getBaron());
        matchingObjectives.setDragon(objectives.getDragon());
        matchingObjectives.setHorde(objectives.getHorde());
        matchingObjectives.setInhibitor(objectives.getInhibitor());
        matchingObjectives.setRiftHerald(objectives.getRiftHerald());
        matchingObjectives.setTower(objectives.getTower());
        return matchingObjectives;
    }

    /**
     * Participants
     */
    private List<MatchingResponseDto.MatchData.SummonerMatchData> mapToParticipants(List<ProcessMatchInfo.Team.ParticipantInfo> participants, int totalKills) {
        List<MatchingResponseDto.MatchData.SummonerMatchData> matchingParticipants = new ArrayList<>();
        for (ProcessMatchInfo.Team.ParticipantInfo participant : participants) {
            MatchingResponseDto.MatchData.SummonerMatchData summonerMatchData = mapToSummonerMatchData(participant, totalKills);
            matchingParticipants.add(summonerMatchData);
        }
        return matchingParticipants;
    }

    private MatchingResponseDto.MatchData.SummonerMatchData mapToSummonerMatchData(ProcessMatchInfo.Team.ParticipantInfo SummonerMatchData, int totalKills) {
        MatchingResponseDto.MatchData.SummonerMatchData matchingSummonerMatchData = new MatchingResponseDto.MatchData.SummonerMatchData();
        matchingSummonerMatchData.setLane(SummonerMatchData.getLane());
        matchingSummonerMatchData.setWin(SummonerMatchData.isWin());
        matchingSummonerMatchData.setBot(SummonerMatchData.isBot());
        matchingSummonerMatchData.setKill(SummonerMatchData.getKill());
        matchingSummonerMatchData.setDeath(SummonerMatchData.getDeath());
        matchingSummonerMatchData.setAssists(SummonerMatchData.getAssist());
        matchingSummonerMatchData.setMinionKill(SummonerMatchData.getMinionKill());
        matchingSummonerMatchData.setCsPerMinute(SummonerMatchData.getCsPerMinute());
        double killParticipation = SummonerMatchData.getKillParticipation();
        if (killParticipation == 0.0) {
            killParticipation = (double) (SummonerMatchData.getKill() + SummonerMatchData.getAssist()) / totalKills * 100;
        }
        matchingSummonerMatchData.setKillParticipation(killParticipation);
        matchingSummonerMatchData.setChampionName(SummonerMatchData.getChampionName());
        matchingSummonerMatchData.setChampionLevel(SummonerMatchData.getChampionLevel());
        matchingSummonerMatchData.setRiotGameName(SummonerMatchData.getRiotName());
        matchingSummonerMatchData.setRiotGameTag(SummonerMatchData.getRiotTag());

        matchingSummonerMatchData.setTotalDamage(SummonerMatchData.getTotalDamage());
        matchingSummonerMatchData.setTotalGold(SummonerMatchData.getTotalGold());
        matchingSummonerMatchData.setVisionWards(SummonerMatchData.getVisionWards()); // 제어와드 구매
        matchingSummonerMatchData.setVisionScore(SummonerMatchData.getVisionScore()); // 시야점수
        matchingSummonerMatchData.setWardPlaced(SummonerMatchData.getWardPlaced()); // 와드 설치
        matchingSummonerMatchData.setSpell1Id(SummonerMatchData.getSpell1Id());
        matchingSummonerMatchData.setSpell2Id(SummonerMatchData.getSpell2Id());
        matchingSummonerMatchData.setPUuid(SummonerMatchData.getPUuid());

        matchingSummonerMatchData.setPerks(mapToPerks(SummonerMatchData.getPerks()));
        matchingSummonerMatchData.setItemNumberList(SummonerMatchData.getItemNumberList());

        return matchingSummonerMatchData;
    }

    private MatchingResponseDto.MatchData.SummonerMatchData.Perks mapToPerks(ProcessMatchInfo.Team.ParticipantInfo.Perks perks) {
        MatchingResponseDto.MatchData.SummonerMatchData.Perks matchingperks = new MatchingResponseDto.MatchData.SummonerMatchData.Perks();
        matchingperks.setMain(mapToPerkDetail(perks.getMain()));
        matchingperks.setSub(mapToPerkDetail(perks.getSub()));
        return matchingperks;
    }

    private MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail mapToPerkDetail(ProcessMatchInfo.Team.ParticipantInfo.Perks.PerkDetail perkDetail) {
        MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail matchingPerkDetail = new MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail();
        matchingPerkDetail.setPerkStyle(perkDetail.getPerkStyle());
        matchingPerkDetail.setPerkIdList(perkDetail.getPerkList());
        return matchingPerkDetail;
    }
}
