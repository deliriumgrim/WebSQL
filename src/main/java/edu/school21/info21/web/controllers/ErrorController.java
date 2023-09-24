package edu.school21.info21.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String error(HttpServletRequest request,
                        Model model,
                        @ModelAttribute("error") String errorMessage) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            model.addAttribute("error", "ERROR " + statusCode);
        } else {
            model.addAttribute("error", errorMessage);
        }
        log.info("Пользователь получил сообщение об ошибке: " + errorMessage);
        return "error/error";
    }

}
