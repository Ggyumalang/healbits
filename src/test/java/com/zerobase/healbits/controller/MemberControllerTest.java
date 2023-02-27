package com.zerobase.healbits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healbits.dto.MemberDto;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.service.MemberService;
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

@WebMvcTest
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
                                        , 2000
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("khg1111@hanmail.net"))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andDo(print());
    }
}