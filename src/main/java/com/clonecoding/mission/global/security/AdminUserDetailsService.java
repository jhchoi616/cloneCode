package com.clonecoding.mission.global.security;

import com.clonecoding.mission.global.entity.Admin;
import com.clonecoding.mission.global.repository.AdminRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("관리자를 찾을 수 없습니다."));

        return User.builder()
                .username(admin.getUserId())
                .password(admin.getPasswd())
                .authorities(
                        new SimpleGrantedAuthority("ROLE_" + admin.getRole())
                )
                .build();
    }
}