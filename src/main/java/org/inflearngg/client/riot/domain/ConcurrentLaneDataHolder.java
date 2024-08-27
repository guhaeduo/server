package org.inflearngg.client.riot.domain;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ConcurrentLaneDataHolder {
    private final AtomicInteger laneGameCnt = new AtomicInteger(0);
    private final AtomicInteger laneWinCnt = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, ConcurrentChampDataHolder> champMap = new ConcurrentHashMap<>();

    public void sumLaneGameCnt(){
        laneGameCnt.incrementAndGet();
    }
    public void sumLaneWinCnt(){
        laneWinCnt.incrementAndGet();
    }
}
