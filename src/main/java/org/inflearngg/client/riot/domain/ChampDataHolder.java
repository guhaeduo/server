package org.inflearngg.client.riot.domain;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicInteger;

public class ChampDataHolder {

    AtomicInteger champGameCnt = new AtomicInteger(0);
    AtomicInteger champWinCnt = new AtomicInteger(0);
    AtomicInteger champGameDuring = new AtomicInteger(0);
    AtomicInteger champCsCnt = new AtomicInteger(0);
    AtomicInteger champVisionScore = new AtomicInteger(0);
    AtomicInteger champKillCnt = new AtomicInteger(0);
    AtomicInteger champAssistCnt = new AtomicInteger(0);
    AtomicInteger champDeathCnt = new AtomicInteger(0);
    AtomicDouble champKda = new AtomicDouble(0);
    AtomicDouble champKillParticipation = new AtomicDouble(0);
}
