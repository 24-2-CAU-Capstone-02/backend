package com.capstone.backend.service;

import com.capstone.backend.entity.Account;
import com.capstone.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String googleId) throws UsernameNotFoundException {
        Account account = accountRepository.findByGoogleId(googleId).orElseThrow(() -> new UsernameNotFoundException(googleId));
        return new User(account.getGoogleId(), "N/A", new ArrayList<>());
    }
}
