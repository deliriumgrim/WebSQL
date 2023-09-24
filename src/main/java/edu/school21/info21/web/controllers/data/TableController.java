package edu.school21.info21.web.controllers.data;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

public interface TableController<T, D> {
    String mainPage(Model model, RedirectAttributes redirectAttributes);

    String add(T entity, RedirectAttributes redirectAttributes);

    String findById(D id, Model model, RedirectAttributes redirectAttributes);

    String update(T entity, RedirectAttributes redirectAttributes);

    String delete(D id, RedirectAttributes redirectAttributes);

    String importData(MultipartFile multipartFile, String tableName, Model model, RedirectAttributes redirectAttributes);

    String exportData(String tableName, HttpServletResponse response, RedirectAttributes redirectAttributes);
}
