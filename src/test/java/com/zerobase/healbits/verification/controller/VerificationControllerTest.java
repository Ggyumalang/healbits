package com.zerobase.healbits.verification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.verification.dto.VerificationDto;
import com.zerobase.healbits.verification.dto.VerificationInfo;
import com.zerobase.healbits.verification.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationController.class)
class VerificationControllerTest {

    @MockBean
    private VerificationService verificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void success_verifyChallenge() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(verificationService.verifyChallenge(anyLong(), any(), anyString()))
                .willReturn(VerificationDto.builder()
                        .email("abcd@1234")
                        .challengeName("challenge")
                        .verifiedDate(LocalDate.now())
                        .imagePath("/image")
                        .build());

        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(multipart("/verification")
                        .file(multipartFile)
                        .file(new MockMultipartFile("verifiedTakeChallengeId", "", "application/json", "1".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abcd@1234"))
                .andExpect(jsonPath("$.challengeName").value("challenge"))
                .andExpect(jsonPath("$.verifiedDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.imagePath").value("/image"));
    }

    @Test
    @WithMockUser
    void success_getVerificationInfo() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(verificationService.getVerificationInfo(anyLong()))
                .willReturn(VerificationInfo.builder()
                        .verifiedCnt(3)
                        .remainVerificationCnt(7)
                        .verificationRate("0.3")
                        .build());

        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(get("/verification?verifiedTakeChallengeId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verifiedCnt").value(3))
                .andExpect(jsonPath("$.remainVerificationCnt").value(7))
                .andExpect(jsonPath("$.verificationRate").value("0.3"));
    }

}