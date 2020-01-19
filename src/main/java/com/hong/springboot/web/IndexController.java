package com.hong.springboot.web;

import com.hong.springboot.config.auth.LoginUser;
import com.hong.springboot.config.auth.dto.SessionUser;
import com.hong.springboot.service.PostsService;
import com.hong.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final HttpSession httpSession;
    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) model.addAttribute("userName", user.getName());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto responseDto = postsService.findById(id);
        model.addAttribute("posts", responseDto);
        return "posts-update";
    }
}
