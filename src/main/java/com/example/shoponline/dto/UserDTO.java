package com.example.shoponline.dto;

import com.example.shoponline.annotations.MyAnnotation;
import lombok.Data;
@Data
public class UserDTO {

    @MyAnnotation(name = "id пользователя")
    private Integer id;
    @MyAnnotation(name = "логин пользователя")
    private String email;
    @MyAnnotation(name = "имя пользователя")
    private String firstName;
    @MyAnnotation(name = "фамилия пользователя")
    private String lastName;
    @MyAnnotation(name = "телефон пользователя")
    private String phone;
    @MyAnnotation(name = "ссылка на аватар пользователя")
    private String image;
}