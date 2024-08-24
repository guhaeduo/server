package org.inflearngg.client.riot.domain;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ConcurrentChampDataHolder {

    private final AtomicInteger champGameCnt = new AtomicInteger(0);
    private final AtomicInteger champWinCnt = new AtomicInteger(0);
    private final AtomicInteger champGameDuring = new AtomicInteger(0);
    private final AtomicInteger champCsCnt = new AtomicInteger(0);
    private final AtomicInteger champVisionScore = new AtomicInteger(0);
    private final AtomicInteger champKillCnt = new AtomicInteger(0);
    private final AtomicInteger champAssistCnt = new AtomicInteger(0);
    private final AtomicInteger champDeathCnt = new AtomicInteger(0);
    private final AtomicDouble champKda = new AtomicDouble(0);
    private final AtomicDouble champKillParticipation = new AtomicDouble(0);


    public void sumChampGameCnt(){
        champGameCnt.incrementAndGet();
    }
    public void sumChampWinCnt(){
        champWinCnt.incrementAndGet();
    }
    public void sumChampGameDuring(int time){
        champGameDuring.addAndGet(time);
    }
    public void sumChampCsCnt(int cs){
        champCsCnt.addAndGet(cs);
    }
    public void sumChampVisionScore(int visionScore){
        champVisionScore.addAndGet(visionScore);
    }
    public void sumChampKillCnt(int kill){
        champKillCnt.addAndGet(kill);
    }
    public void sumChampAssistCnt(int assist){
        champAssistCnt.addAndGet(assist);
    }
    public void sumChampDeathCnt(int death){
        champDeathCnt.addAndGet(death);
    }
    public void sumChampKda(double kda){
        champKda.addAndGet(kda);
    }
    public void sumChampKillParticipation(double killParticipation){
        champKillParticipation.addAndGet(killParticipation);
    }
}
