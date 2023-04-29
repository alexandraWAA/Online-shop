package com.example.shoponline.dto;

import com.example.shoponline.annotations.MyAnnotation;
import com.example.shoponline.enums.Role;
import lombok.Data;

@Data
public class RegisterReq {
    @MyAnnotation(name = "логин")
    private String username;
    @MyAnnotation(name = "пароль")
    private String password;
    @MyAnnotation(name = "имя пользователя")
    private String firstName;
    @MyAnnotation(name = "фамилия пользователя")
    private String lastName;
    @MyAnnotation(name = "телефон пользователя")
    private String phone;
    @MyAnnotation(name = "роль пользователя")
    private Role role;
}
