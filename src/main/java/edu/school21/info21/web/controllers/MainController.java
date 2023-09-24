package edu.school21.info21.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        log.info("Пользователь получил главную страницу");
        return "index";
    }

    @GetMapping("/data")
    public String dataPage() {
        log.info("Пользователь получил главную страницу данных");
        return "navigation/data";
    }

    @GetMapping("/operations")
    public String operationsPage() {
        log.info("Пользователь получил главную страницу операций");
        return "navigation/operations";
    }
}
