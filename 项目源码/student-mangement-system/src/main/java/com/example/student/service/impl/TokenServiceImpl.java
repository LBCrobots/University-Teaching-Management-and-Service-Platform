package com.example.student.service.impl;

import com.example.student.repository.TokenMapper;
import com.example.student.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenMapper tokenMapper;

    @Override
    public void save(String token) {
        tokenMapper.save(token);
    }

    @Override
    public String getToken() {
        return tokenMapper.get();
    }

    @Override
    public void deleteToken() {
        tokenMapper.delete();
    }
}
