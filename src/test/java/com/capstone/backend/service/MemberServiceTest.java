package com.capstone.backend.service;

import com.capstone.backend.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService();
    }

    @Test
    void getMemberById() {
        Member member = memberService.getMemberById(1L);
        assertNotNull(member);

    }

    @Test
    void updateMember() {
    }
}