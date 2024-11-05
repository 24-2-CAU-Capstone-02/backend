package com.capstone.backend.service;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    public Member getMemberById(Long memberId) {
        return null;
    }

    public void updateMember(Member member, MemberUpdateRequest request) {
    }
}
