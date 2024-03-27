package org.inflearngg.duo.dto;

public enum Tier {

    ALL, UNRANKED, IRON, BRONZE, SILVER, GOLD, PLATINUM, EMERALD , DIAMOND, MASTER, GRANDMASTER, CHALLENGER;

    public static String InputValueTierCheck(String inputTier) {
        String tier = inputTier.toUpperCase();
        if(tier.equals("ALL")) {
            return null;
        }
        return Tier.valueOf(inputTier).name();
    }

}
