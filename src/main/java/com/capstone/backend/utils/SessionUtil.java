package com.capstone.backend.utils;

import com.capstone.backend.entity.Member;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionUtil {
    private final MemberRepository memberRepository;

    public Member getMemberBySessionToken(String sessionToken) throws CustomException {
        return memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_TOKEN_IS_INVALID));
    }

    public String generateUUIDToken() {
        return UUID.randomUUID().toString();
    }
}
