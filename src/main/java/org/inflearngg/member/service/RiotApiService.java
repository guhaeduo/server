package org.inflearngg.member.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class RiotApiService {

    private final RestTemplate restTemplate;

    public RiotApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String getPuuid(String gameName, String tagLine) {
        String url = String.format("https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=RGAPI-1bfe6024-13ac-4942-84ed-2ff511ce8d9a", gameName, tagLine);
        ResponseEntity<RiotAccount> response = restTemplate.getForEntity(url, RiotAccount.class);
        return response.getBody().getPuuid();
    }
}
