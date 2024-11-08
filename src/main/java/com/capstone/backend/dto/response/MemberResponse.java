package com.capstone.backend.dto.response;

import com.capstone.backend.entity.Member;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String username;

    public static MemberResponse getMemberResponse(Member member) throws CustomException {
        try {
            return MemberResponse.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MAPPING_ERROR);
        }
    }
}
