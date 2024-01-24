package org.inflearngg.match.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

public class MatchInfo {

    // 현재 조회중인 소환사 데이터는 별도로 저장해야됨. currentSummonerMatchData
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiData{
        private Object metadata;
        private MatchBasicInfo info;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MatchBasicInfo {
        private String gameDuration; // 게임 시간
        private String gameEndTimestamp;// 게임 종료 시간(밀리초 단위)
        private String gameStartTimestamp;// 게임 시작 시간(밀리초 단위)
        private String gameMode; // 게임모드  CLASSIC, ARAM, URF, ONEFORALL, ASCENSION, FIRSTBLOOD, KINGPORO
        //        private boolean quickShutdown;
//        private String maxDamage;
        private ParticipantInfo[] participants;
        private String queueId; //queueType 랭크유무
        private String mapId;
        private String seasonId;
        private String gameVersion;
        private String gameType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ParticipantInfo {
        private int kills; // 총킬수
        private int killingSprees; // 연속킬
        private int deaths; // 죽은 총 횟수
        private int assists; // 어시스트
        private int totalMinionsKilled; // 미니언 킬
        private String championName; // 챔피언 이름
        private String championId; // 챔피언 id
        private String champLevel; // 챔피언 id
        private String riotIdGameName; // 게임 이름
        private String riotIdTagline; // 소환사 태그
        private String teamPosition; // 팀 포지션(JUNGLE ,TOP ,MID ,BOTTOM ,SUPPORT)
        private int goldEarned; // 골드 획득량
        private int totalDamageDealtToChampions; // 챔피언에게 가한 피해량
        private int totalDamageTaken; // 받은 피해량
        private int totalHeal; // 치유량
        private int visionScore; // 시야점수
        private int visionWardsBoughtInGame; // 핑와 구매 횟수
        private int item0; // 아이템 0
        private int item1; // 아이템 1
        private int item2; // 아이템 2
        private int item3; // 아이템 3
        private int item4; // 아이템 4
        private int item5; // 아이템 5
        private int item6; // 악세사리 Warding Totem (Trinket)
        private int wardsPlaced; // 와드 설치 횟수
        private int summoner1Casts; // 스펠1 사용 횟수
        private int summoner1Id; // 스펠1 id
        private int summoner2Casts; // 스펠2 사용 횟수
        private int summoner2Id; // 스펠2 id
        private String puuid; // puuid
        private Perks perks;
        private Team[] teams;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Perks {
        private StatePerks statPerks;
        private Style[] styles; // 메인룬, 보조룬 배열
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class StatePerks { // 스탯 룬
        private int defense; // 방어
        private int flex; // 유틸
        private int offense; // 공격
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Style { // 룬
        private String description; // 메인(primaryStyle)인지 서브(subStyle)인지
        private int style; // 룬 스타일 (지배, 결의, 영감, 마법(8200), 정밀(8000))
        private Selections[] selections; // 선택한 룬의 ID
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Selections { // 룬
        private int perk; // 선택한 id(정밀의 경우, 집공, 침착, )
        private int var1; // 해당룬으로 얻은 게임 내 성과 데이터
        private int var2;
        private int var3;
    }

    // 직접구해야됨.
    //    private int totalKill;
    //    private int totalDeath;
    //    private int totalAssists;
    //    private int totalGold;
    //   private int maxDamage;
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Team {
        private Objectives objectives;
        private int teamId; // 팀 id( 100 또는 200)
        private boolean win; // 승리여부

    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Objectives {
        private Baron baron;
        private Dragon dragon;
        private Horde horde;
        private Inhibitor inhibitor;
        private RiftHerald riftHerald;
        private Tower tower;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Baron{ // 바론
        private boolean first;
        private int kills;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Dragon{ // 드레곤
        private boolean first;
        private int kills;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Horde{  // 유충 파괴수
        private boolean first;
        private int kills;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Inhibitor{ // 억제기 파괴수
        private boolean first;
        private int kills;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class RiftHerald{ // 전령 획득 수
        private boolean first;
        private int kills;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    private static class Tower{ // 타워수
        private boolean first;
        private int kills;
    }
}
