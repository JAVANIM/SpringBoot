package com.boot.board_240214.controller;

import com.boot.board_240214.model.User;
import com.boot.board_240214.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account")
@Slf4j
public class AccountController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        log.info("@# login()");

        return "account/login";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "account/register";
    }

    @PostMapping(value = "/register")
    public String register(User user) {
        userService.save(user);

        return "account/login";
    }

}


