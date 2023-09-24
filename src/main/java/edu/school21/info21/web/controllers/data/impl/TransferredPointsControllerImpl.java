package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.TransferredPointsController;
import edu.school21.info21.web.models.data.TransferredPointEntity;
import edu.school21.info21.web.services.data.TransferredPointService;
import edu.school21.info21.web.services.operations.ProcedureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
public class TransferredPointsControllerImpl implements TransferredPointsController {

    private final TransferredPointService transferredPointService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    public TransferredPointsControllerImpl(TransferredPointService transferredPointService,
                                           ProcedureService procedureService,
                                           @Value("${import.path}") String importPath,
                                           @Value("${export.path}") String exportPath,
                                           @Value("${export.filename}") String exportFilename) {
        this.transferredPointService = transferredPointService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/transferredPoints")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TransferredPointEntity> list = this.transferredPointService.findAll();
            model.addAttribute("transferredPointList", list);
            log.info("Пользователь успешно получил главную страницу таблицы Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Transferred_Points. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/transferred_points";
    }

    @Override
    @PostMapping("/addTransferredPoint")
    public String add(TransferredPointEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            System.out.println(entity);
            this.transferredPointService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Transferred_Points. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:transferredPoints";
    }

    @Override
    @GetMapping("findTransferredPoint")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<TransferredPointEntity> byId = this.transferredPointService.findById(id);
            model.addAttribute("transferredPointList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Transferred_Points. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/transferred_points";
    }

    @Override
    @PostMapping("/updateTransferredPoint")
    public String update(TransferredPointEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.transferredPointService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Transferred_Points. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:transferredPoints";
    }

    @Override
    @PostMapping("/deleteTransferredPoint")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.transferredPointService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Transferred_Points. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:transferredPoints";
    }

    @Override
    @PostMapping("/importTransferredPoints")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Transferred_Points");
            return "redirect:/error";
        }
        return "redirect:transferredPoints";
    }

    @Override
    @GetMapping("/exportTransferredPoints")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Transferred_Points");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Transferred_Points");
            return "redirect:/error";
        }
        return null;
    }
}
