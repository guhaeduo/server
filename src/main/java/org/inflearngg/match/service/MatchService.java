package org.inflearngg.match.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.match.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;


    @Value("${spring.api.key}")
    private String API_KEY;
    // puuid, cnt, queue

    //전적갯수
    private final int MATCH_CNT = 30;
    String API_URL_MathIdList = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?count={cnt}";
    String API_URL_MathData = "https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}";
    private final RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String[]> response = null;


    public String[] getMatchIdsByPuuid(String puuid, int queueType) {
        // Header에 API키 셋팅
        headers.set("X-Riot-Token", API_KEY);
        try {
            if (queueType != 0) {
                API_URL_MathIdList += "&queue={queue}";
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, MATCH_CNT, queueType);
            } else
                response = restTemplate.exchange(API_URL_MathIdList, HttpMethod.GET, entity, String[].class, puuid, MATCH_CNT);
            if (response.getStatusCode().is2xxSuccessful()) {

                return response.getBody();
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }

    }

    public List<ProcessingMatchInfo.MatchData> getMatchDataList(String[] matchIdList, String puuid) {
        ArrayList<CompletableFuture<ProcessingMatchInfo.MatchData>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        int i = 0;
        LocalDateTime startTime = LocalDateTime.now();
        log.info("[비동기 시작]");
        for (String matchId : matchIdList) {
            futures.add(CompletableFuture.supplyAsync(() -> getMatchData(matchId))
                    .thenApply(completableFuture -> {
                        MatchInfo.MatchBasicInfo riotApiData = null;
                        try {
                            riotApiData = completableFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                        ProcessingMatchInfo.MatchData processingData = new ProcessingMatchInfo.MatchData();
                        ProcessingMatchInfo.MaxDamage maxDamage = new ProcessingMatchInfo.MaxDamage();
                        ProcessingMatchInfo.Team redTeam = new ProcessingMatchInfo.Team();
                        ProcessingMatchInfo.Team blueTeam = new ProcessingMatchInfo.Team();
                        setProcessingTeamAndCurrentSummonerAndMaxDamageData(riotApiData.getParticipants(), maxDamage, redTeam, blueTeam, processingData, puuid);
                        // 오브젝트 계산, 승리여부도 추가
                        setObjectiveAndIsWinData(riotApiData.getTeams(), redTeam, blueTeam);
                        setProcessingInfoData(riotApiData, maxDamage, processingData);
                        processingData.setRed(redTeam);
                        processingData.setBlue(blueTeam);
                        return processingData;
                    }));
        }
        executorService.shutdown();
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }

    private static void setProcessingInfoData(MatchInfo.MatchBasicInfo riotApiData, ProcessingMatchInfo.MaxDamage maxDamage, ProcessingMatchInfo.MatchData processingData) {
        ProcessingMatchInfo.Info info = new ProcessingMatchInfo.Info();
        setMatchGameInfo(info, riotApiData, maxDamage);
        processingData.setInfo(info);
    }

    /**
     * 이따 private로 바꿔야됨
     */

    @Async
    public CompletableFuture<MatchInfo.MatchBasicInfo> getMatchData(String matchId) {

        headers.set("X-Riot-Token", API_KEY);
        try {
            ResponseEntity<MatchInfo.ApiData> matchInfo = restTemplate.exchange(API_URL_MathData, HttpMethod.GET, entity, MatchInfo.ApiData.class, matchId);
            if (matchInfo.getStatusCode().is2xxSuccessful()) {
//                내용전부 출력해보기
//                String threadName = Thread.currentThread().getName();
//                log.info("Thread name: " + threadName);
                return CompletableFuture.completedFuture(matchInfo.getBody().getInfo());
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }

    private static void setMatchGameInfo(ProcessingMatchInfo.Info info, MatchInfo.MatchBasicInfo matchData, ProcessingMatchInfo.MaxDamage maxDamage) {
        info.setGameDuration(Integer.parseInt(matchData.getGameDuration()));
        info.setGameEndStamp(matchData.getGameEndTimestamp());
        info.setQueueType(matchData.getQueueId());
        //다시하기 유무

        info.setMaxDamage(maxDamage);
    }

    private void setProcessingTeamAndCurrentSummonerAndMaxDamageData(MatchInfo.ParticipantInfo[] participants, ProcessingMatchInfo.MaxDamage maxDamage, ProcessingMatchInfo.Team redTeam, ProcessingMatchInfo.Team blueTeam, ProcessingMatchInfo.MatchData matchData, String curSummonerPuuid) {
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

        for (MatchInfo.ParticipantInfo p : participants) {
            participant = setProcessingParticipantInfo(p);
            if (p.getPuuid().equals(curSummonerPuuid)) {
                matchData.setCurrentSummonerMatchData(participant);
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
            }
        }

        setTeamData(redTeam, redTotalKill, redTotalDeath, redTotalAssists, redTotalGold, redMaxDamage);
        setTeamData(blueTeam, blueTotalKill, blueTotalDeath, blueTotalAssists, blueTotalGold, blueMaxDamage);
    }

    private static ProcessingMatchInfo.ParticipantInfo setProcessingParticipantInfo(MatchInfo.ParticipantInfo p) {
        ProcessingMatchInfo.ParticipantInfo participantInfo = new ProcessingMatchInfo.ParticipantInfo();
        participantInfo.setLane(p.getTeamPosition());
        participantInfo.setKill(p.getKills());
        participantInfo.setDeath(p.getDeaths());
        participantInfo.setAssist(p.getAssists());
        participantInfo.setMinionKill(p.getTotalMinionsKilled());
        participantInfo.setChampionName(p.getChampionName());
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
        setPerks(p.getPerks().getStyles(), participantInfo.getPerks());
        participantInfo.setVisionWards(p.getVisionWardsBoughtInGame());
        participantInfo.setVisionScore(p.getVisionScore());
        participantInfo.setWardPlaced(p.getWardsPlaced());
        participantInfo.setSpell1Id(p.getSummoner1Id());
        participantInfo.setSpell2Id(p.getSummoner2Id());
        participantInfo.setPUuid(p.getPuuid());

        return participantInfo;
    }

    private static void setPerks(MatchInfo.Style[] styles, ProcessingMatchInfo.Perks perks) {
        for (MatchInfo.Style style : styles) {
            if (style.description == "primaryStyle") {
                perks.setMain(
                        new ProcessingMatchInfo.PerkDetail(
                                style.getStyle(),
                                new ArrayList<>() {
                                    {
                                        for (MatchInfo.Selections selection : style.getSelections()) {
                                            add(selection.getPerk());
                                        }
                                    }
                                }
                        ));
            }
            if (style.description == "subStyle") {
                perks.setSub(
                        new ProcessingMatchInfo.PerkDetail(
                                style.getStyle(),
                                new ArrayList<>() {
                                    {
                                        for (MatchInfo.Selections selection : style.getSelections()) {
                                            add(selection.getPerk());
                                        }
                                    }
                                }
                        )
                );
            }
        }

    }

    private static void setObjectiveAndIsWinData(MatchInfo.Team[] teams, ProcessingMatchInfo.Team redTeam, ProcessingMatchInfo.Team blueTeam) {
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].getTeamId() == 100) { //blueTeam
                setObjectives(teams[i], blueTeam);
                blueTeam.setWin(teams[i].isWin());
            } else { // 200 redTeam
                setObjectives(teams[i], redTeam);
            }
        }
    }

    private static void setObjectives(MatchInfo.Team teams, ProcessingMatchInfo.Team team) {
        team.setObjectives(new ProcessingMatchInfo.Objectives());
        team.getObjectives().setBaron(teams.getObjectives().getBaron().getKills());
        team.getObjectives().setDragon(teams.getObjectives().getDragon().getKills());
        team.getObjectives().setHorde(teams.getObjectives().getRiftHerald().getKills());
        team.getObjectives().setInhibitor(teams.getObjectives().getInhibitor().getKills());
        team.getObjectives().setRiftHerald(teams.getObjectives().getRiftHerald().getKills());
        team.getObjectives().setTower(teams.getObjectives().getTower().getKills());
    }

    private void setTeamData(ProcessingMatchInfo.Team redTeam, int redTotalKill, int redTotalDeath, int redTotalAssists, int redTotalGold, int redMaxDamage) {
        redTeam.setTotalKill(redTotalKill);
        redTeam.setTotalDeath(redTotalDeath);
        redTeam.setTotalAssists(redTotalAssists);
        redTeam.setTotalGold(redTotalGold);
        redTeam.setTeamMaxDamage(redMaxDamage);
    }


}
