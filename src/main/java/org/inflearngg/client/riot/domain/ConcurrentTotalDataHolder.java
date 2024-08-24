package org.inflearngg.client.riot.domain;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class ConcurrentTotalDataHolder {
    private final AtomicInteger totalGameCnt = new AtomicInteger(0);
    private final AtomicInteger totalWinCnt = new AtomicInteger(0);
    private final AtomicInteger totalLoseCnt = new AtomicInteger(0);
    private final AtomicDouble totalKda = new AtomicDouble(0);
    private final AtomicInteger totalKillCnt = new AtomicInteger(0);
    private final AtomicInteger totalDeathCnt = new AtomicInteger(0);
    private final AtomicInteger totalAssistCnt = new AtomicInteger(0);
    private final AtomicInteger totalCsCnt = new AtomicInteger(0);

    public void sumTotalGameCnt(){
        totalGameCnt.incrementAndGet();
    }
    public void sumTotalWinCnt(){
        totalWinCnt.incrementAndGet();
    }
    public void sumTotalLoseCnt(){
        totalLoseCnt.incrementAndGet();
    }
    public void sumTotalKda(double kda){
        totalKda.addAndGet(kda);
    }
    public void sumTotalKillCnt(int kill){
        totalKillCnt.addAndGet(kill);
    }
    public void sumTotalDeathCnt(int death){
        totalDeathCnt.addAndGet(death);
    }
    public void sumTotalAssistCnt(int assist){
        totalAssistCnt.addAndGet(assist);
    }
    public void sumTotalCsCnt(int cs){
        totalCsCnt.addAndGet(cs);
    }

}
