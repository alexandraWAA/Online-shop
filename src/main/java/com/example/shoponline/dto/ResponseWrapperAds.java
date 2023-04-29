package com.example.shoponline.dto;

import com.example.shoponline.annotations.MyAnnotation;
import lombok.Data;

import java.util.List;
@Data
public class ResponseWrapperAds {
    @MyAnnotation(name = "общее количество объявлений")
    private Integer count;
    private List<AdsDTO> result;
}
