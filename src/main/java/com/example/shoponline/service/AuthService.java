package com.example.shoponline.service;

import com.example.shoponline.dto.RegisterReq;

public interface AuthService {
    boolean login(String username, String password);

    boolean register(RegisterReq registerReq);
}
