package com.hong.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
//@WebMvcTest
public class ControllerTest {

//    @Autowired
//    private MockMvc mvc;

    @Test
    public void test() {
        String expected = "test";

        assertThat(expected)
                .isEqualTo("test");
    }

    @Test
    public void mvcTest() {
        String text = null;
        if("test".equals(text)) System.out.println("==============> ok");
    }

}
