package com.human.face.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @GetMapping("/add")
    public int addTwoNumber(
            @RequestParam int num1,
            @RequestParam int num2
    ) {
        return num1 + num2;
    }
}
