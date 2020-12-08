package com.springboot.sample.controller;

import com.springboot.sample.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping("/findUserPageList")
    public Object findUserPageList(){
        return userService.findUserPageList2();
    }
}
