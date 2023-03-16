package com.zerobase.healbits.resttemplate;

import com.zerobase.healbits.exception.HealBitsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.zerobase.healbits.type.ErrorCode.TRANSACTION_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateApi {
    private final RestTemplate restTemplate;
    private final static String baseUrl = "http://localhost:8080/%s/%s";

    private static final String TRANSACTION = "transaction";
    private static final String USE = "use";

    public void callUseBalance(long participationFee, String token) {
        log.info("CallUseBalance started " + LocalDateTime.now());
        String url = String.format(baseUrl,TRANSACTION, USE);

        HttpEntity<CallUseBalance.Request> requestEntity = RequestEntity
                .post(url)
                .header("Authorization", token)
                .body(CallUseBalance.Request.builder()
                        .participationFee(participationFee)
                        .build());

        HttpEntity<CallUseBalance.Response> response = restTemplate.postForEntity(url, requestEntity, CallUseBalance.Response.class);
        log.info("Response : {} ", response);
        if (Objects.requireNonNull(response.getBody()).getStatus() != 0) {
            throw new HealBitsException(TRANSACTION_SERVER_ERROR);
        }
        log.info("CallUseBalance finished " + LocalDateTime.now());
    }
}
