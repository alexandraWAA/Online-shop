package com.example.shoponline.dto;

import com.example.shoponline.annotations.MyAnnotation;
import lombok.Data;
@Data
public class LoginReq {
    @MyAnnotation(name = "пароль")
    private String password;
    @MyAnnotation(name = "логин")
    private String username;

}
