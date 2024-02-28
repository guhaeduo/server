package org.inflearngg.summoner.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class AccountClient {

    @Value("${spring.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    // Header에 API키 셋팅
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String ACCOUNT_API_URL;

    public RiotApiResponseDto.RiotPuuid fetchPuuidAPI(String gameName, String gameTag, String region) {
        // Header에 API키 셋팅
        headers.set("X-Riot-Token", API_KEY);
        ACCOUNT_API_URL = "https://" + region + ".api.riotgames.com/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}";
        try {
            ResponseEntity<RiotApiResponseDto.RiotPuuid> response = restTemplate.exchange(ACCOUNT_API_URL, HttpMethod.GET, entity, RiotApiResponseDto.RiotPuuid.class, gameName, gameTag);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            if (response.getStatusCode().is4xxClientError()) {
                throw new HttpClientErrorException(response.getStatusCode());
            } else {
                throw new RuntimeException("소환사 정보를 가져오는데 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류가 발생 했습니다.", e);
        }
    }
}
