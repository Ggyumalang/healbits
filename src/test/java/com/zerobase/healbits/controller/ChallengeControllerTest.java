package com.zerobase.healbits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.challenge.controller.ChallengeController;
import com.zerobase.healbits.challenge.dto.ChallengeDto;
import com.zerobase.healbits.challenge.dto.ChallengeSummaryInfo;
import com.zerobase.healbits.challenge.dto.RegisterChallenge;
import com.zerobase.healbits.challenge.service.ChallengeService;
import com.zerobase.healbits.common.type.ChallengeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        given(challengeService.registerChallenge(any(), anyString()))
                .willReturn(ChallengeDto.builder()
                        .email("abc@daum.net")
                        .challengeName("challenge")
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
                                        "chall",
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
                .andExpect(jsonPath("$.challengeName").value("challenge"));
    }

    @Test
    @WithMockUser
    void success_getChallengeListByCategory() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "name";
        Sort sort = Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);

        List<ChallengeSummaryInfo> challengeSummaryInfoList = List.of(
                ChallengeSummaryInfo.builder()
                        .challengeName("취미갖기")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .summary("취미")
                        .participantsNum(0)
                        .challengeDuration(7)
                        .remainingDaysToStart(1)
                        .build()
                , ChallengeSummaryInfo.builder()
                        .challengeName("취미갖기2")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .summary("취미2")
                        .participantsNum(0)
                        .challengeDuration(13)
                        .remainingDaysToStart(3)
                        .build());

        Slice<ChallengeSummaryInfo> pageList = new SliceImpl<>(challengeSummaryInfoList, pageable, false);
        given(challengeService.getChallengeListByCategory(anyString(), any()))
                .willReturn(pageList);
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(get("/challenge/list?challengeCategory=HOBBY"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].challengeName").value("취미갖기"))
                .andExpect(jsonPath("$.content[0].challengeCategory").value("HEALTH"))
                .andExpect(jsonPath("$.content[0].challengeDuration").value(7))
                .andExpect(jsonPath("$.content[0].remainingDaysToStart").value(1))
                .andExpect(jsonPath("$.content[1].challengeName").value("취미갖기2"))
                .andExpect(jsonPath("$.content[1].challengeCategory").value("HEALTH"))
                .andExpect(jsonPath("$.content[1].challengeDuration").value(13))
                .andExpect(jsonPath("$.content[1].remainingDaysToStart").value(3));
    }

    @Test
    @WithMockUser
    void success_getChallengeDetail() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(challengeService.getChallengeDetail(anyLong()))
                .willReturn(
                        ChallengeDto.builder()
                                .challengeName("challenge")
                                .challengeCategory(ChallengeCategory.HEALTH)
                                .contents("abcdef")
                                .participantsNum(0)
                                .startDate(LocalDate.now())
                                .endDate(LocalDate.now().minusDays(1))
                                .build()
                );
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(get("/challenge/detail?challengeId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challengeName").value("challenge"))
                .andExpect(jsonPath("$.challengeCategory").value("HEALTH"))
                .andExpect(jsonPath("$.contents").value("abcdef"))
                .andExpect(jsonPath("$.participantsNum").value(0))
                .andExpect(jsonPath("$.challengeStatus").value("CLOSED"));
    }
}