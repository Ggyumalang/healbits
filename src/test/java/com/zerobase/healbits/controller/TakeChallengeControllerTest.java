package com.zerobase.healbits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.takechallenge.controller.TakeChallengeController;
import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeDto;
import com.zerobase.healbits.takechallenge.service.TakeChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TakeChallengeController.class)
class TakeChallengeControllerTest {
    @MockBean
    private TakeChallengeService takeChallengeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void success_participateChallenge() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(takeChallengeService.participateChallenge(any(), any()))
                .willReturn(TakeChallengeDto.builder()
                        .email("abc@hanmail.net")
                        .challengeName("challenge")
                        .participationFee(1000)
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(post("/take-challenge/participate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ParticipateChallenge.Request(
                                        1, 500
                                ))
                        ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@hanmail.net"))
                .andExpect(jsonPath("$.challengeName").value("challenge"))
                .andExpect(jsonPath("$.participationFee").value(1000));

    }
}