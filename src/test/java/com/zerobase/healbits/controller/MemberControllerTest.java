package com.zerobase.healbits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.dto.*;
import com.zerobase.healbits.member.controller.MemberController;
import com.zerobase.healbits.member.dto.LoginMember;
import com.zerobase.healbits.member.dto.MemberDto;
import com.zerobase.healbits.member.dto.MemberInfo;
import com.zerobase.healbits.member.dto.RegisterMember;
import com.zerobase.healbits.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void success_RegisterMember() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(memberService.registerMember(any()))
                .willReturn(MemberDto.builder()
                        .email("khg1111@hanmail.net")
                        .name("홍길동")
                        .phone("01011112222")
                        .balance(1000)
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(post("/member/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegisterMember.Request(
                                        "1234@1234"
                                        , "1234"
                                        , "허준"
                                        , "01022223333"
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("khg1111@hanmail.net"))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void success_LoginMember() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(memberService.login(anyString(), anyString()))
                .willReturn(TokenInfo.builder()
                        .grantType("Bearer")
                        .accessToken("1234")
                        .refreshToken("1234")
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(post("/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginMember(
                                        "khg2154@naver.com"
                                        , "1234"
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("1234"))
                .andExpect(jsonPath("$.refreshToken").value("1234"))
                .andDo(print());
    }

    @Test
    @DisplayName("No Authority Access")
    public void fail_index_anonymous() throws Exception {
        mockMvc.perform(get("/member/info?email=jake@naver.com")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("USER Authority Access")
    public void success_index_user() throws Exception {
        mockMvc.perform(get("/member/info?email=jake@naver.com")
                        .with(user("jake").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void success_getMemberInfo() throws Exception {
        //given 어떤 데이터가 주어졌을 때
        given(memberService.getMemberInfo(anyString()))
                .willReturn(MemberInfo.builder()
                        .email("khg1111@hanmail.net")
                        .name("홍길동")
                        .phone("01011112222")
                        .balance(1000)
                        .build());
        //when 어떤 경우에
        //then 이런 결과가 나온다.
        mockMvc.perform(get("/member/info?email=jake@naver.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("khg1111@hanmail.net"))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.phone").value("01011112222"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

}