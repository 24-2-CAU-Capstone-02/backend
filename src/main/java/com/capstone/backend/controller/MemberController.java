package com.capstone.backend.controller;

import com.capstone.backend.dto.request.MemberUpdateRequest;
import com.capstone.backend.entity.Member;
import com.capstone.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "멤버 관련 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "멤버 정보 수정", description = "멤버의 이름 및 비밀번호를 수정합니다.")
    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable("memberId") String memberId, @Valid @RequestBody MemberUpdateRequest request) {
        Member member = memberService.getMemberById(memberId);
        memberService.updateMember(member, request);
        return ResponseEntity.ok().build();
    }
}
