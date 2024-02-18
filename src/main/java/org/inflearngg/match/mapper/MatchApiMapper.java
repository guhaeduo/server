package org.inflearngg.match.mapper;

import org.inflearngg.match.client.RiotAPIMatchInfo;
import org.inflearngg.match.dto.process.ProcessMatchInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.inflearngg.match.dto.process.ProcessMatchInfo.*;

@Component
public class MatchApiMapper {

    //client.RiotAPIMatchInfo.ApiData -> dto.process.ProcessMatchInfo

    public ProcessMatchInfo mapRiotAPIToProcessMatchInfo(RiotAPIMatchInfo.ApiData apiData, String curPuuid) {
        ProcessMatchInfo processMatchInfo = new ProcessMatchInfo();
        processMatchInfo.setInfo(mapRiotAPIToInfo(apiData.getInfo()));
        processMatchInfo.setCurrentSummonerParticipantInfo(new Team.ParticipantInfo());
        processMatchInfo.setBlue(new Team());
        processMatchInfo.setRed(new Team());
        setRedTeamAndBlueTeamAndMaxDamageAndCurrentSummoner(processMatchInfo.getRed(), processMatchInfo.getBlue(), apiData.getInfo().getTeams(), apiData.getInfo().getParticipants(), processMatchInfo.getInfo().getMaxDamage(), processMatchInfo, curPuuid);

        return processMatchInfo;
    }

//    public


    //client.RiotAPIMatchInfo.MatchBasicInfo -> dto.process.ProcessMatchInfo.Info
    public Info mapRiotAPIToInfo(RiotAPIMatchInfo.MatchBasicInfo matchBasicInfo) {
        Info info = new Info();
        info.setGameDuration(matchBasicInfo.getGameDuration());
        info.setGameEndStamp(matchBasicInfo.getGameEndTimestamp());
        info.setQueueType(matchBasicInfo.getQueueId());

        info.setQuickShutdown(matchBasicInfo.getGameDuration() <= 300);
        info.setMaxDamage(new Info.MaxDamage());
        return info;
    }


    //client.RiotAPIMatchInfo.ParticipantInfo -> dto.process.ProcessMatchInfo.Team.ParticipantInfo


    //client.RiotAPIMatchInfo.Team -> dto.process.ProcessMatchInfo.Team
    private void setRedTeamAndBlueTeamAndMaxDamageAndCurrentSummoner(Team red, Team blue, RiotAPIMatchInfo.Team[] teams, RiotAPIMatchInfo.ParticipantInfo[] participants, Info.MaxDamage maxDamage, ProcessMatchInfo info, String curPuuid) {
        // 이때, 참가자 정보랑 MaxDamage 정보를 매핑해야함.
        //참가자 정보 매핑
        for (RiotAPIMatchInfo.ParticipantInfo part : participants) {
            if (part.getPuuid().equals(curPuuid)) {
                info.setCurrentSummonerParticipantInfo(mapToParticipantInfo(part));
            }
            if (maxDamage.getDamage() < part.getTotalDamageDealtToChampions()) {
                maxDamage.setMaxDamage(part.getTotalDamageDealtToChampions(), part.getRiotIdGameName(), part.getRiotIdTagline(), part.getChampionName(), part.getChampionId());
            }
            if (part.getTeamId() == 100) {
                mapToTeamParticipantInfo(part, blue);
            } else {
                mapToTeamParticipantInfo(part, red);
            }

        }

        // 오브젝트 매핑
        for (RiotAPIMatchInfo.Team team : teams) {
            if (team.getTeamId() == 100) { // blueTeam
                mapToTeamObjectInfo(blue, team);

            } else { // redTeam
                mapToTeamObjectInfo(red, team);
            }
        }


    }

    private static void mapToTeamObjectInfo(Team blue, RiotAPIMatchInfo.Team team) {
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

    private static void mapToTeamParticipantInfo(RiotAPIMatchInfo.ParticipantInfo part, Team team) {
        team.addTotalKills(part.getKills());
        team.addTotalDeaths(part.getDeaths());
        team.addTotalAssists(part.getAssists());
        team.addTotalGold(part.getGoldEarned());
        team.setMaxDamage(part.getTotalDamageDealtToChampions(), team.getTeamMaxDamage());
        Team.ParticipantInfo participantInfo = mapToParticipantInfo(part);
        team.addParticipant(participantInfo);
    }

    @NotNull
    private static Team.ParticipantInfo mapToParticipantInfo(RiotAPIMatchInfo.ParticipantInfo part) {
        // part -> participantInfo
        Team.ParticipantInfo participantInfo = new Team.ParticipantInfo();
        participantInfo.setLane(part.getTeamPosition());
//queueType = 850이면 봇전이다.
        participantInfo.setKill(part.getKills());
        participantInfo.setDeath(part.getDeaths());
        participantInfo.setAssist(part.getAssists());
        participantInfo.setMinionKill(part.getTotalMinionsKilled());
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
    private static Team.ParticipantInfo.Perks mapToPerks(RiotAPIMatchInfo.ParticipantInfo part) {
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
