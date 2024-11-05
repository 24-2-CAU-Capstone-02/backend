package com.capstone.backend.service;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.entity.Member;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username("testUser")
                .password("testPass")
                .sessionToken("testToken")
                .build();
    }

    @Test
    void getMemberById_Success() {
        // given
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        Member result = memberService.getMemberById(memberId);

        // then
        assertNotNull(result);
        assertEquals(member.getUsername(), result.getUsername());
        verify(memberRepository, times(1)).findById(Long.valueOf(memberId));
    }

    @Test
    void getMemberById_Failure() {
        // given
        Long memberId = 2L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> memberService.getMemberById(memberId));
        verify(memberRepository, times(1)).findById(Long.valueOf(memberId));
    }

    @Test
    void updateMember_Success() {
        // given
        MemberUpdateRequest request = new MemberUpdateRequest("newToken", "newUser", "newPass");
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when
        memberService.updateMember(member, request);

        // then
        assertEquals("newToken", member.getSessionToken());
        assertEquals("newUser", member.getUsername());
        assertEquals("newPass", member.getPassword());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void updateMember_Failure() {
        // given
        MemberUpdateRequest request = new MemberUpdateRequest("newToken", "newUser", "newPass");
        Member nonExistentMember = Member.builder().id(99L).build();

        when(memberRepository.findById(nonExistentMember.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> memberService.updateMember(nonExistentMember, request));

        verify(memberRepository, times(1)).findById(nonExistentMember.getId());
        verify(memberRepository, never()).save(any());
    }
}