package com.hong.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.springboot.domain.posts.Posts;
import com.hong.springboot.domain.posts.PostsRepository;
import com.hong.springboot.web.dto.PostsResponseDto;
import com.hong.springboot.web.dto.PostsSaveRequestDto;
import com.hong.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @After
    public void tearDwon() {
        postsRepository.deleteAll();
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(springSecurity())
                .build();
    }

    @Test
    public void posts_수정된다() {
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("inyong")
                .build()
        );

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();
        HttpEntity<PostsUpdateRequestDto> requestDtoHttpEntity = new HttpEntity<>(requestDto);
        String url = "http://localhost:"+ port + "/api/v1/posts/"+ updateId;
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestDtoHttpEntity, Long.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        PostsResponseDto responseEntity1 = restTemplate.getForObject(url+"/{id}", PostsResponseDto.class, updateId);
        assertThat(responseEntity1.getTitle()).isEqualTo(expectedTitle);
        assertThat(responseEntity1.getContent()).isEqualTo(expectedContent);
    }

    @Test
    public void posts_조회된다 () throws Exception {
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .build();
        String url = "http://localhost:"+port+"/api/v1/posts";
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Long id = responseEntity.getBody();

        mvc.perform(get("/api/v1/posts/"+id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("content").value(content));

        String result = restTemplate.getForObject(url+"/{id}", String.class, id);
        PostsResponseDto resultDto = objectMapper.readValue(result, PostsResponseDto.class);
        PostsResponseDto result2 = restTemplate.getForObject(url+"/{id}", PostsResponseDto.class, id);

        assertThat(resultDto.getTitle()).isEqualTo(title);
        assertThat(resultDto.getContent()).isEqualTo(content);
    }

    @Test
    public void posts_등록된다 () {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author("test@gmail.com")
                                                            .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity= restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

}