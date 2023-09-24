package edu.school21.info21.web.controllers.operations.impl;

import edu.school21.info21.web.controllers.operations.SqlExecutorController;
import edu.school21.info21.web.models.operations.QueryResultEntity;
import edu.school21.info21.web.services.operations.SqlExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/operations")
public class SqlExecutorControllerImpl implements SqlExecutorController {

    private final SqlExecutorService sqlExecutorService;

    @Autowired
    public SqlExecutorControllerImpl(SqlExecutorService sqlExecutorService) {
        this.sqlExecutorService = sqlExecutorService;
    }

    @Override
    @GetMapping("/sqlExecutor")
    public String getPage() {
        log.info("Пользователь получил главную страницу SQL Executor");
        return "operations/sql_executor";
    }

    @Override
    @GetMapping("/executeQuery")
    public String sqlExecutor(@RequestParam(value = "sqlQuery", required = false) String query,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            QueryResultEntity queryResult = this.sqlExecutorService.executeQuery(query);
            if (queryResult != null) {
                model.addAttribute("columns", queryResult.getColumns());
                model.addAttribute("rows", queryResult.getRows());
            }
            model.addAttribute("backPage", "sqlExecutor");
            log.info("Пользователь успешно запустил запрос в SQL Executor");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог запустить запрос в SQL Executor. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "operations/sql_query_result";
    }
}
