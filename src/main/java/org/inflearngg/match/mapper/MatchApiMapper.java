package org.inflearngg.match.mapper;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.dto.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.inflearngg.match.dto.process.ProcessMatchInfo.*;



@Slf4j
@Component
public class MatchApiMapper {

    //client.RiotAPIMatchInfo.ApiData -> dto.process.ProcessMatchInfo

    public ProcessMatchInfo mapRiotAPIToProcessMatchInfo(RiotAPIMatchInfo.MatchBasicInfo riotApiData, String curPuuid, String matchId){
        if (riotApiData == null) {
            return null;
        }
        ProcessMatchInfo processMatchInfo = new ProcessMatchInfo();
        processMatchInfo.setMatchId(matchId);
        processMatchInfo.setInfo(mapRiotAPIToInfo(riotApiData));
        processMatchInfo.setCurrentSummonerParticipantInfo(new Team.ParticipantInfo());
        processMatchInfo.setBlue(new Team());
        processMatchInfo.setRed(new Team());
        setRedTeamAndBlueTeamAndMaxDamageAndCurrentSummoner(processMatchInfo.getRed(), processMatchInfo.getBlue(), riotApiData.getTeams(), riotApiData.getParticipants(), processMatchInfo.getInfo().getMaxData(), processMatchInfo, curPuuid);

        return processMatchInfo;
    }

//    public


    //client.RiotAPIMatchInfo.MatchBasicInfo -> dto.process.ProcessMatchInfo.Info
    public Info mapRiotAPIToInfo(RiotAPIMatchInfo.MatchBasicInfo matchBasicInfo) {
        if (matchBasicInfo == null) {
            return null;
        }
        Info info = new Info();
        info.setGameDuration(matchBasicInfo.getGameDuration());
        info.setGameEndStamp(matchBasicInfo.getGameEndTimestamp());
        info.setQueueType(matchBasicInfo.getQueueId());
        info.setQuickShutdown(matchBasicInfo.getGameDuration() <= 300);
        Info.MaxData maxData = new Info.MaxData();
        maxData.initMaxData();
        info.setMaxData(maxData);
        return info;
    }


    //client.RiotAPIMatchInfo.ParticipantInfo -> dto.process.ProcessMatchInfo.Team.ParticipantInfo


    //client.RiotAPIMatchInfo.Team -> dto.process.ProcessMatchInfo.Team
    private void setRedTeamAndBlueTeamAndMaxDamageAndCurrentSummoner(Team red, Team blue, RiotAPIMatchInfo.Team[] teams, RiotAPIMatchInfo.ParticipantInfo[] participants, Info.MaxData maxData, ProcessMatchInfo info, String curPuuid) {
        // 이때, 참가자 정보랑 MaxDamage 정보를 매핑해야함.
        //참가자 정보 매핑
        int minute = info.getInfo().getGameDuration() / 60;
        for (RiotAPIMatchInfo.ParticipantInfo part : participants) {
            // 현재 유저 정보
            if (part.getPuuid().equals(curPuuid)) {
                info.setCurrentSummonerParticipantInfo(mapToParticipantInfo(part, minute));
                maxData.defaultMaxData(part.getRiotIdGameName(), part.getRiotIdTagline(), part.getChampionName());
            }
            // 최대 데미지
            if (maxData.getMaxDamage().getDamage() < part.getTotalDamageDealtToChampions()) {
                maxData.getMaxDamage().setMaxDamage(part.getTotalDamageDealtToChampions(), part.getRiotIdGameName(), part.getRiotIdTagline(), part.getChampionName());
            }
            // 최대 킬
            if(maxData.getMaxKill().getKill() < part.getKills()){
                maxData.getMaxKill().setMaxKill(part.getKills(), part.getRiotIdGameName(), part.getRiotIdTagline(),part.getChampionName());
            }
            // 최대 데스
            if(maxData.getMaxDeath().getDeath() < part.getDeaths()){
                maxData.getMaxDeath().setMaxDeath(part.getDeaths(), part.getRiotIdGameName(), part.getRiotIdTagline(),part.getChampionName());
            }
            // 최대 어시스트
            if (maxData.getMaxAssist().getAssist() < part.getAssists()){
                maxData.getMaxAssist().setMaxAssist(part.getAssists(), part.getRiotIdGameName(), part.getRiotIdTagline(), part.getChampionName());
            }
            // 팀 정보 매핑
            if (part.getTeamId() == 100) {
                mapToTeamParticipantInfo(part, blue, minute);
            } else {
                mapToTeamParticipantInfo(part, red, minute);
            }

        }
        // 팀 총킬 세팅

        // 오브젝트 매핑
        for (RiotAPIMatchInfo.Team team : teams) {
            if (team.getTeamId() == 100) { // blueTeam
                mapToTeamObjectInfo(blue, team);

            } else { // redTeam
                mapToTeamObjectInfo(red, team);
            }
        }


    }

    private void mapToTeamObjectInfo(Team blue, RiotAPIMatchInfo.Team team) {
        blue.setTeamName(team.getTeamId());
        blue.setWin(team.isWin());
        blue.setObjectives(
                new Team.Objectives(
                        team.getObjectives().getBaronKills(),
                        team.getObjectives().getDragonKills(),
                        team.getObjectives().getHordeKills(),
                        team.getObjectives().getInhibitorKills(),
                        team.getObjectives().getRiftHeraldKills(),
                        team.getObjectives().getTowerKills()
                )
        );
    }

    private void mapToTeamParticipantInfo(RiotAPIMatchInfo.ParticipantInfo part, Team team, int minute) {
        team.addTotalKills(part.getKills());
        team.addTotalDeaths(part.getDeaths());
        team.addTotalAssists(part.getAssists());
        team.addTotalGold(part.getGoldEarned());
        team.setMaxDamage(part.getTotalDamageDealtToChampions(), team.getTeamMaxDamage());
        Team.ParticipantInfo participantInfo = mapToParticipantInfo(part, minute);
        team.addParticipant(participantInfo);
    }

    @NotNull
    private Team.ParticipantInfo mapToParticipantInfo(RiotAPIMatchInfo.ParticipantInfo part, int minute) {
        // part -> participantInfo
        Team.ParticipantInfo participantInfo = new Team.ParticipantInfo();
        participantInfo.setLane(part.getTeamPosition());
        participantInfo.setWin(part.isWin());
        participantInfo.setTeamId(part.getTeamId());
//queueType = 850이면 봇전이다.
        participantInfo.setKill(part.getKills());
        participantInfo.setDeath(part.getDeaths());
        participantInfo.setAssist(part.getAssists());
        participantInfo.setMinionKill(part.getTotalMinionsKilled());
        participantInfo.setCsPerMinute((double) (part.getTotalMinionsKilled() + part.getNeutralMinionsKilled()) /minute);
        participantInfo.setKillParticipation(part.getChallenges().getKillParticipation() * 100);
        participantInfo.setChampionName(part.getChampionName());
        participantInfo.setChampionIconNumber(part.getChampionId());
        participantInfo.setChampionLevel(part.getChampLevel());
        participantInfo.setRiotName(part.getRiotIdGameName());
        participantInfo.setRiotTag(part.getRiotIdTagline());
        participantInfo.setTotalDamage(part.getTotalDamageDealtToChampions());
        participantInfo.setTotalGold(part.getGoldEarned());
        participantInfo.setItemNumberList(
                new ArrayList<>(
                        List.of(
                                part.getItem0(),
                                part.getItem1(),
                                part.getItem2(),
                                part.getItem3(),
                                part.getItem4(),
                                part.getItem5(),
                                part.getItem6()
                        )
                )
        );
        Team.ParticipantInfo.Perks perks = mapToPerks(part);
        participantInfo.setPerks(perks);

        participantInfo.setVisionWards(part.getVisionWardsBoughtInGame());
        participantInfo.setVisionScore(part.getVisionScore());
        participantInfo.setWardPlaced(part.getWardsPlaced());
        participantInfo.setSpell1Id(part.getSummoner1Id());
        participantInfo.setSpell2Id(part.getSummoner2Id());
        participantInfo.setPUuid(part.getPuuid());
        return participantInfo;
    }

    @NotNull
    private Team.ParticipantInfo.Perks mapToPerks(RiotAPIMatchInfo.ParticipantInfo part) {
        Team.ParticipantInfo.Perks perks = new Team.ParticipantInfo.Perks();
        for (RiotAPIMatchInfo.ParticipantInfo.Perks.Style style : part.getPerks().getStyles()) {
            if (style.getDescription().equals("primaryStyle")) {
                perks.setMain(style.getStyle(), style.getRuneIdList());
            }
            if (style.getDescription().equals("subStyle")) {
                perks.setSub(style.getStyle(), style.getRuneIdList());
            }
        }
        return perks;
    }


}
