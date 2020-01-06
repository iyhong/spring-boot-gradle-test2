package com.hong.springboot.service;

import com.hong.springboot.domain.posts.Posts;
import com.hong.springboot.domain.posts.PostsRepository;
import com.hong.springboot.web.dto.PostsResponseDto;
import com.hong.springboot.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 없습니다."));
        return new PostsResponseDto(entity);
    }
}
