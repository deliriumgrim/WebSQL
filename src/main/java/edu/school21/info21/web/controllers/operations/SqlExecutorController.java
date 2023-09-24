package edu.school21.info21.web.controllers.operations;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface SqlExecutorController {

    String getPage();

    String sqlExecutor(String query, Model model, RedirectAttributes redirectAttributes);

}
