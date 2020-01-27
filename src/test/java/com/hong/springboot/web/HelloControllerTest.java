package com.hong.springboot.web;

import com.hong.springboot.config.auth.SecurityConfig;
import com.hong.springboot.web.dto.HelloResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@WebMvcTest(value = HelloController.class,
            excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
            }
)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "USER")
    public void hello가_리턴된다 () throws Exception {
        mvc.perform(get("/hello"))

                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    public void 롬복_기능_테스트 () {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void helloDto가_리턴된다 () throws Exception {
        //given
        String name = "test";
        int amount = 100;

        //when, then
        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

    }
}