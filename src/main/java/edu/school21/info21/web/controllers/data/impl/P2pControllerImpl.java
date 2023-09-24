package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.P2pController;
import edu.school21.info21.web.models.data.P2pEntity;
import edu.school21.info21.web.services.data.P2pService;
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
public class P2pControllerImpl implements P2pController {

    private final P2pService p2pService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public P2pControllerImpl(P2pService p2pService,
                             ProcedureService procedureService,
                             @Value("${import.path}") String importPath,
                             @Value("${export.path}") String exportPath,
                             @Value("${export.filename}") String exportFilename) {
        this.p2pService = p2pService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/p2p")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<P2pEntity> all = this.p2pService.findAll();
            model.addAttribute("p2pList", all);
            log.info("Пользователь успешно получил главную страницу таблицы P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы P2P. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/p2p";
    }

    @Override
    @PostMapping("/addP2p")
    public String add(P2pEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.p2pService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу P2P. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:p2p";
    }

    @Override
    @GetMapping("/findP2p")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<P2pEntity> byId = this.p2pService.findById(id);
            model.addAttribute("p2pList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/p2p";
    }

    @Override
    @PostMapping("/updateP2p")
    public String update(P2pEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.p2pService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице P2P. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:p2p";
    }

    @Override
    @PostMapping("/deleteP2p")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.p2pService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы P2P. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:p2p";
    }

    @Override
    @PostMapping("/importP2p")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу P2P");
            return "redirect:/error";
        }
        return "redirect:p2p";
    }

    @Override
    @GetMapping("/exportP2p")
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
            log.info("Пользователь успешно экспортировал данные из таблицы P2P");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы P2P");
            return "redirect:/error";
        }
        return null;
    }
}
