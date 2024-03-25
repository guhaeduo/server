package org.inflearngg.aop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.inflearngg.duo.dto.Lane;

import java.util.Arrays;

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


    public static Region findByDescription(String description, String name) {
        return Arrays.stream(Region.values())
                .filter(region -> region.name().equals(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 리전이 없습니다."+ name + " = " +description));
    }
    @JsonCreator
    public static Region findByDescription(String description) {
        return Arrays.stream(Region.values())
                .filter(region -> region.name().equals(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 리전이 없습니다. " +description));
    }
}


