package com.capstone.backend.service;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.entity.Member;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MemberRepository;
import com.capstone.backend.utils.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SessionUtil sessionUtil;

    public Member getMemberById(Long memberId) throws CustomException {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void updateMember(Member member, MemberUpdateRequest request) throws CustomException {
        Member tokenMember = sessionUtil.getMemberBySessionToken(request.getSessionToken());
        if (!Objects.equals(member.getId(), tokenMember.getId())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORIZED);
        }

        if (request.getUsername() != null) {
            member.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            member.setPassword(request.getPassword());
        }

        memberRepository.save(member);
    }
}
