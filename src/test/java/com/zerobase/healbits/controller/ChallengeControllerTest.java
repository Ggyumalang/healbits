package com.zerobase.healbits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.dto.ChallengeDto;
import com.zerobase.healbits.dto.RegisterChallenge;
import com.zerobase.healbits.service.ChallengeService;
import com.zerobase.healbits.type.ChallengeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest {

    @MockBean
    private ChallengeService challengeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void success_registerChallenge() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(challengeService.registerChallenge(any()))
                .willReturn(ChallengeDto.builder()
                        .email("abc@daum.net")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .summary("abc")
                        .contents("abcdef")
                        .participantsNum(0)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(7))
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(post("/challenge/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegisterChallenge.Request(
                                        "abc@naver.com",
                                        "LIFE",
                                        "요약입니다",
                                        "내용입니다",
                                        LocalDate.now(),
                                        LocalDate.now().plusDays(14)
                                )
                        )).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@daum.net"))
                .andExpect(jsonPath("$.challengeCategory").value("HEALTH"))
                .andExpect(jsonPath("$.summary").value("abc"))
                .andExpect(jsonPath("$.startDate").value("2023-03-06"))
                .andExpect(jsonPath("$.endDate").value("2023-03-13"));
    }
}