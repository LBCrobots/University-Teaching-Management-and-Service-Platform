package com.example.student.service;

public interface TokenService {
    public void save(String token);

    public String getToken();

    public void deleteToken();
}
