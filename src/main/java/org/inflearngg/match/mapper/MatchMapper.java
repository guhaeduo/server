package org.inflearngg.match.mapper;

import org.inflearngg.match.dto.response.MatchingResponseDto;
import org.inflearngg.match.service.processing.ProcessingData;
import org.inflearngg.match.service.processing.ProcessingMatchInfo;
import org.inflearngg.match.service.processing.ProcessingRankInfo;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class MatchMapper {

    //mapper
    public MatchingResponseDto mapToMatchingResponseDto(ProcessingData processingData) {
        MatchingResponseDto matchingResponseDto = new MatchingResponseDto();
        matchingResponseDto.setSummonerRankInfo(mapToSummonerRankInfo(processingData.getSummonerRankInfo()));
        matchingResponseDto.setMatchDataList(mapToMatchDataList(processingData.getMatchDataList()));
        return matchingResponseDto;
    }

    /**
     * summonerRankInfo
     */
    private MatchingResponseDto.SummonerRankInfo mapToSummonerRankInfo(ProcessingRankInfo.SummonerRankInfo summonerRankInfo) {
        MatchingResponseDto.SummonerRankInfo matchingSummonerRankInfo = new MatchingResponseDto.SummonerRankInfo();
        matchingSummonerRankInfo.setInfo(mapToRankInfo(summonerRankInfo.getInfo()));
        matchingSummonerRankInfo.setLane(mapToLane(summonerRankInfo.getLane()));
        return matchingSummonerRankInfo;
    }

    private MatchingResponseDto.SummonerRankInfo.RankInfo mapToRankInfo(ProcessingRankInfo.SummonerRankInfo.RankInfo rankInfo) {
        MatchingResponseDto.SummonerRankInfo.RankInfo matchingRankInfo = new MatchingResponseDto.SummonerRankInfo.RankInfo();
        matchingRankInfo.setWinningRate((double) rankInfo.getWins() / rankInfo.getCntGame() * 100);
        matchingRankInfo.setWins(rankInfo.getWins());
        matchingRankInfo.setLoses(rankInfo.getLoses());
        matchingRankInfo.setKda(rankInfo.getKda() / rankInfo.getCntGame());
        matchingRankInfo.setKillAvg(rankInfo.getTotalKills() / rankInfo.getCntGame());
        matchingRankInfo.setDeathAvg(rankInfo.getTotalDeaths() / rankInfo.getCntGame());
        matchingRankInfo.setAssistAvg(rankInfo.getTotalAssists() / rankInfo.getCntGame());
        return matchingRankInfo;
    }

    private MatchingResponseDto.SummonerRankInfo.Lane mapToLane(ProcessingRankInfo.SummonerRankInfo.Lane lane) {
        MatchingResponseDto.SummonerRankInfo.Lane matchingLane = new MatchingResponseDto.SummonerRankInfo.Lane();
        matchingLane.setAll(mapToRankLaneData(lane.getAll()));
        matchingLane.setTop(mapToRankLaneData(lane.getTop()));
        matchingLane.setJug(mapToRankLaneData(lane.getJug()));
        matchingLane.setMid(mapToRankLaneData(lane.getMid()));
        matchingLane.setAdc(mapToRankLaneData(lane.getAdc()));
        matchingLane.setSup(mapToRankLaneData(lane.getSup()));
        return matchingLane;
    }

    private MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData mapToRankLaneData(ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData rankLaneData) {
        MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData matchingRankLaneData = new MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData(0);
        if(rankLaneData.getTotalGameCnt() == 0) {
            matchingRankLaneData.setMostChampionlist(new ArrayList<>());
            return matchingRankLaneData;
        }
        matchingRankLaneData.setWinningRate((double) rankLaneData.getWins() / rankLaneData.getTotalGameCnt() * 100);
        matchingRankLaneData.setMostChampionlist(mapToMostChampionList(
                        rankLaneData.getChampions().values().stream()
                                .sorted(Comparator.comparingInt(ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData.ChampionData::getTotalGameCnt).reversed())
                                .limit(3)
                                .collect(Collectors.toList())
                )
        );
        return matchingRankLaneData;
    }

    private List<MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion> mapToMostChampionList(List<ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData.ChampionData> mostChampionList) {
        List<MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion> matchingMostChampionList = new ArrayList<>();
        for (ProcessingRankInfo.SummonerRankInfo.Lane.RankLaneData.ChampionData championData : mostChampionList) {
            MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion matchingMostChampion = new MatchingResponseDto.SummonerRankInfo.Lane.RankLaneData.MostChampion(0,0,0,0,0);
            if(championData.getTotalGameCnt() != 0) {
                int gameMinuteTime = championData.getGameTime() / 60;
                matchingMostChampion.setChampionName(championData.getChampionName());
                matchingMostChampion.setChampionIconNumber(championData.getChampionIconNumber());
                matchingMostChampion.setWinningRate((double) championData.getWins() / championData.getTotalGameCnt() * 100);
                matchingMostChampion.setCsPerMinute((double) championData.getTotalCS() / gameMinuteTime);
                matchingMostChampion.setVisionScorePerMinute(championData.getVisionScorePerMinute() / championData.getTotalGameCnt());
                matchingMostChampion.setKda(championData.getKda() / championData.getTotalGameCnt());
                matchingMostChampion.setTotalKillParticipation(championData.getTotalKillParticipation() / championData.getTotalGameCnt());
            }

            matchingMostChampionList.add(matchingMostChampion);
        }
        return matchingMostChampionList;
    }

    /**
     * MatchDataList
     */
    private List<MatchingResponseDto.MatchData> mapToMatchDataList(List<ProcessingMatchInfo.MatchData> matchDataList) {
        List<MatchingResponseDto.MatchData> matchingMatchDataList = new ArrayList<>();
        for (ProcessingMatchInfo.MatchData matchData : matchDataList) {
            MatchingResponseDto.MatchData matchingMatchData = new MatchingResponseDto.MatchData();
            matchingMatchData.setInfo(mapToInfo(matchData.getInfo()));
            matchingMatchData.setCurrentSummonerMatchData(mapToSummonerMatchData(matchData.getCurrentSummonerRankInfo()));
            matchingMatchData.setRed(mapToTeam(matchData.getRed()));
            matchingMatchData.setBlue(mapToTeam(matchData.getBlue()));
            matchingMatchDataList.add(matchingMatchData);
        }
        return matchingMatchDataList;
    }

    private MatchingResponseDto.MatchData.MatchInfo mapToInfo(ProcessingMatchInfo.Info info) {
        MatchingResponseDto.MatchData.MatchInfo matchingInfo = new MatchingResponseDto.MatchData.MatchInfo();
        matchingInfo.setGameDuration(info.getGameDuration());
        matchingInfo.setQueueType(info.getQueueType());
        matchingInfo.setQuickShutdown(info.isQuickShutdown());
        matchingInfo.setMaxDamage(mapToMaxDamage(info.getMaxDamage()));
        return matchingInfo;
    }

    private MatchingResponseDto.MatchData.MatchInfo.MaxDamage mapToMaxDamage(ProcessingMatchInfo.Info.MaxDamage maxDamage) {
        MatchingResponseDto.MatchData.MatchInfo.MaxDamage matchingMaxDamage = new MatchingResponseDto.MatchData.MatchInfo.MaxDamage();
        matchingMaxDamage.setChampionName(maxDamage.getChampionName());
        matchingMaxDamage.setChampionIconNumber(maxDamage.getChampionIconNumber());
        matchingMaxDamage.setDamage(maxDamage.getDamage());
        matchingMaxDamage.setRiotGameName(maxDamage.getRiotGameName());
        matchingMaxDamage.setRiotGameTag(maxDamage.getRiotGameTag());
        return matchingMaxDamage;
    }

    /**
     * Team
     */
    public MatchingResponseDto.MatchData.Team mapToTeam(ProcessingMatchInfo.Team team) {
        MatchingResponseDto.MatchData.Team matchingTeam = new MatchingResponseDto.MatchData.Team();
        matchingTeam.setTotalKills(team.getTotalKills());
        matchingTeam.setTotalDeaths(team.getTotalDeaths());
        matchingTeam.setTotalAssists(team.getTotalAssists());
        matchingTeam.setTotalGold(team.getTotalGold());
        matchingTeam.setTeamMaxDamage(team.getTeamMaxDamage());
        matchingTeam.setWin(team.isWin());
        matchingTeam.setObjectives(mapToObjects(team.getObjectives()));
        matchingTeam.setParticipants(mapToParticipants(team.getParticipants()));
        return matchingTeam;
    }

    private MatchingResponseDto.MatchData.Team.Objectives mapToObjects(ProcessingMatchInfo.Objectives objectives) {
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
    private List<MatchingResponseDto.MatchData.SummonerMatchData> mapToParticipants(List<ProcessingMatchInfo.ParticipantInfo> participants) {
        List<MatchingResponseDto.MatchData.SummonerMatchData> matchingParticipants = new ArrayList<>();
        for (ProcessingMatchInfo.ParticipantInfo participant : participants) {
            MatchingResponseDto.MatchData.SummonerMatchData summonerMatchData = mapToSummonerMatchData(participant);
            matchingParticipants.add(summonerMatchData);
        }
        return matchingParticipants;
    }

    private MatchingResponseDto.MatchData.SummonerMatchData mapToSummonerMatchData(ProcessingMatchInfo.ParticipantInfo SummonerMatchData) {
        MatchingResponseDto.MatchData.SummonerMatchData matchingSummonerMatchData = new MatchingResponseDto.MatchData.SummonerMatchData();
        matchingSummonerMatchData.setPosition(SummonerMatchData.getLane());
        matchingSummonerMatchData.setBot(SummonerMatchData.isBot());
        matchingSummonerMatchData.setKill(SummonerMatchData.getKill());
        matchingSummonerMatchData.setDeath(SummonerMatchData.getDeath());
        matchingSummonerMatchData.setMinionKill(SummonerMatchData.getMinionKill());

        matchingSummonerMatchData.setChampionName(SummonerMatchData.getChampionName());
        matchingSummonerMatchData.setChampionIconNumber(SummonerMatchData.getChampionIconNumber());
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

    private MatchingResponseDto.MatchData.SummonerMatchData.Perks mapToPerks(ProcessingMatchInfo.Perks perks) {
        MatchingResponseDto.MatchData.SummonerMatchData.Perks matchingperks = new MatchingResponseDto.MatchData.SummonerMatchData.Perks();
        matchingperks.setMain(mapToPerkDetail(perks.getMain()));
        matchingperks.setSub(mapToPerkDetail(perks.getSub()));

        return matchingperks;
    }

    private MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail mapToPerkDetail(ProcessingMatchInfo.PerkDetail perkDetail) {
        MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail matchingPerkDetail = new MatchingResponseDto.MatchData.SummonerMatchData.Perks.PerkDetail();
        matchingPerkDetail.setPerkStyle(perkDetail.getPerkStyle());
        matchingPerkDetail.setPerkIdList(perkDetail.getPerkList());
        return matchingPerkDetail;
    }
}
