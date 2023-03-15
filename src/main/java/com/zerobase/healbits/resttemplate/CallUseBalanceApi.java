//TODO
package com.zerobase.healbits.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CallUseBalanceApi {
    private final RestTemplate restTemplate;

    public void call(long participationFee, String token) {
        // Set up headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token.substring(7));

        // Set up the request entity with any request body if needed
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("participationFee", String.valueOf(participationFee));
        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        // Set up the URL of the internal API
        String url = "http://localhost:8080/transaction/use";

        // Make the request using the RestTemplate
        HttpEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(response);
    }
}
