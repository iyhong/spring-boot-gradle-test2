package com.hong.springboot.service;

import com.hong.springboot.domain.posts.Posts;
import com.hong.springboot.domain.posts.PostsRepository;
import com.hong.springboot.web.dto.PostsResponseDto;
import com.hong.springboot.web.dto.PostsSaveRequestDto;
import com.hong.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 사용자가 없습니다. id="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }
}
