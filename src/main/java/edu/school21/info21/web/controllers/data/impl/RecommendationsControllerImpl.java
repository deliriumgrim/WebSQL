package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.RecommendationsController;
import edu.school21.info21.web.models.data.RecommendationEntity;
import edu.school21.info21.web.services.data.RecommendationService;
import edu.school21.info21.web.services.operations.ProcedureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/data")
public class RecommendationsControllerImpl implements RecommendationsController {

    private final RecommendationService recommendationService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public RecommendationsControllerImpl(RecommendationService recommendationService,
                                         ProcedureService procedureService,
                                         @Value("${import.path}") String importPath,
                                         @Value("${export.path}") String exportPath,
                                         @Value("${export.filename}") String exportFilename) {
        this.recommendationService = recommendationService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/recommendations")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<RecommendationEntity> all = this.recommendationService.findAll();
            model.addAttribute("recommendationList", all);
            log.info("Пользователь успешно получил главную страницу таблицы Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/recommendations";
    }

    @Override
    @PostMapping("/addRecommendation")
    public String add(RecommendationEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.recommendationService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:recommendations";
    }

    @Override
    @GetMapping("/findRecommendation")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<RecommendationEntity> byId = this.recommendationService.findById(id);
            model.addAttribute("recommendationList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/recommendations";
    }

    @Override
    @PostMapping("/updateRecommendation")
    public String update(RecommendationEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.recommendationService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:recommendations";
    }

    @Override
    @PostMapping("/deleteRecommendation")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.recommendationService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Recommendations. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:recommendations";
    }

    @Override
    @PostMapping("importRecommendations")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Recommendations");
            return "redirect:/error";
        }
        return "redirect:recommendations";
    }

    @Override
    @GetMapping("/exportRecommendations")
    public String exportData(@RequestParam(value = "tableName", required = false) String tableName,
                             HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.exportData(tableName, this.exportPath);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=export.csv");
            InputStream inputStream = Files.newInputStream(Paths.get(this.exportPath));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            log.info("Пользователь успешно экспортировал данные из таблицы Recommendations");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Recommendations");
            return "redirect:/error";
        }
        return null;
    }
}
