package com.example.shoponline.dto;

import com.example.shoponline.annotations.MyAnnotation;
import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapperComment {
    @MyAnnotation(name = "общее количество комментариев")
    private Integer count;
    private List<CommentDTO> result;
}
