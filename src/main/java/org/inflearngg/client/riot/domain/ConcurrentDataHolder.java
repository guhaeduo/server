package org.inflearngg.client.riot.domain;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentDataHolder {
    AtomicInteger totalGameCnt = new AtomicInteger(0);
    AtomicInteger totalWinCnt = new AtomicInteger(0);
    AtomicInteger totalLoseCnt = new AtomicInteger(0);
    AtomicDouble totalKda = new AtomicDouble(0);
    AtomicInteger totalKillCnt = new AtomicInteger(0);
    AtomicInteger totalDeathCnt = new AtomicInteger(0);
    AtomicInteger totalAssistCnt = new AtomicInteger(0);
    AtomicInteger totalCsCnt = new AtomicInteger(0);
}
