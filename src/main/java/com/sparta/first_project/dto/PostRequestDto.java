package com.sparta.first_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자 이하 입니다.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "동영상 URL을 입력해주세요.")
    private String url;
    private String author;

    public void addAuthor(String author) {
        this.author = author;
    }

}