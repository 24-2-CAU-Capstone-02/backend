package com.capstone.backend.service;

import com.capstone.backend.dto.request.GoogleLoginRequest;
import com.capstone.backend.dto.response.JwtResponse;
import com.capstone.backend.entity.Account;
import com.capstone.backend.exception.CustomException;
import com.capstone.backend.exception.ErrorCode;
import com.capstone.backend.repository.AccountRepository;
import com.capstone.backend.utils.GoogleOAuthUtil;
import com.capstone.backend.utils.JwtProviderUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtProviderUtil jwtProviderUtil;
    private final UserDetailsService userDetailsService;
    private final GoogleOAuthUtil googleOAuthUtil;

    public Account googleLogin(@Valid GoogleLoginRequest request) throws CustomException {
        String googleAccessToken;
        try {
            googleAccessToken = googleOAuthUtil.fetchGoogleAccessToken(request.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GOOGLE_FETCH_ACCESS_TOKEN_FAIL);
        }

        LinkedHashMap<String, Object> response;
        try {
            response = googleOAuthUtil.fetchGoogleUserData(googleAccessToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GOOGLE_FETCH_USER_DATA_FAIL);
        }

        String googleId = response.get("id").toString();
        String name = response.get("name").toString();

        Account existedAccount = accountRepository.findByGoogleId(googleId).orElse(null);
        if (existedAccount != null) {
            return existedAccount;
        }

        Account newAccount = Account.builder()
                .name(name)
                .googleId(googleId)
                .dailyRoomCount(0)
                .build();
        accountRepository.save(newAccount);
        return newAccount;
    }

    public JwtResponse generateJwt(Account account) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(account.getGoogleId());
        } catch (UsernameNotFoundException e) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String accessToken = jwtProviderUtil.generateToken(authentication);

        return JwtResponse.builder()
                .accessToken("Bearer " + accessToken)
                .build();
    }
}
