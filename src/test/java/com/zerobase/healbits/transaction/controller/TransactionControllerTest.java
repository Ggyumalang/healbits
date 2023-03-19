package com.zerobase.healbits.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.transaction.dto.ChargeBalance;
import com.zerobase.healbits.transaction.dto.TransactionDto;
import com.zerobase.healbits.transaction.service.TransactionService;
import com.zerobase.healbits.type.TransactionResultType;
import com.zerobase.healbits.type.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void success_chargeBalance() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(transactionService.chargeBalance(any(), anyString()))
                .willReturn(TransactionDto.builder()
                        .email("abc@naver.com")
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.SUCCESS)
                        .transactionId("1")
                        .amount(1000)
                        .balanceSnapshot(1500)
                        .transactedDateTime(LocalDateTime.now())
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(post("/transaction/charge")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ChargeBalance.Request(
                                        5000
                                )
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@naver.com"))
                .andExpect(jsonPath("$.transactionResultType").value("SUCCESS"))
                .andExpect(jsonPath("$.transactionId").value("1"))
                .andExpect(jsonPath("$.chargeAmount").value(1000))
                .andExpect(jsonPath("$.balanceSnapshot").value(1500));
    }

}