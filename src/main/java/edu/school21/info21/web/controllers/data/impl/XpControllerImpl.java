package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.XpController;
import edu.school21.info21.web.models.data.XpEntity;
import edu.school21.info21.web.services.data.XpService;
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
public class XpControllerImpl implements XpController {

    private final XpService xpService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public XpControllerImpl(XpService xpService,
                            ProcedureService procedureService,
                            @Value("${import.path}") String importPath,
                            @Value("${export.path}") String exportPath,
                            @Value("${export.filename}") String exportFilename) {
        this.xpService = xpService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/xp")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<XpEntity> xps = this.xpService.findAll();
            model.addAttribute("xpList", xps);
            log.info("Пользователь успешно получил главную страницу таблицы Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Xp. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/xp";
    }

    @Override
    @PostMapping("/addXp")
    public String add(XpEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.xpService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Xp. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:xp";
    }

    @Override
    @GetMapping("/findXp")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<XpEntity> byId = this.xpService.findById(id);
            model.addAttribute("xpList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Xp. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/xp";
    }

    @Override
    @PostMapping("/updateXp")
    public String update(XpEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.xpService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Xp. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:xp";
    }

    @Override
    @PostMapping("/deleteXp")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.xpService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Xp. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:xp";
    }

    @Override
    @PostMapping("/importXp")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Xp");
            return "redirect:/error";
        }
        return "redirect:xp";
    }

    @Override
    @GetMapping("/exportXp")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Xp");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Xp");
            return "redirect:/error";
        }
        return null;
    }
}
