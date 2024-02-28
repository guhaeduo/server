package org.inflearngg.aop.dto;

import lombok.Getter;

@Getter
public enum Region {

    na1("AMERICAS"),
    br1("AMERICAS"),
    la1("AMERICAS"),
    la2("AMERICAS"),

    kr("ASIA"),
    jp1("ASIA"),

    eun1("EUROPE"),
    euw1("EUROPE"),
    tr1("EUROPE"),
    ru("EUROPE"),

    oc1("SEA"),
    ph2("SEA"),
    sg2("SEA"),
    th2("SEA"),
    tw2("SEA"),
    vn2("SEA");

    private final String continent;

    Region(String continent) {
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }


}

