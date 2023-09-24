package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.PeersController;
import edu.school21.info21.web.models.data.PeerEntity;
import edu.school21.info21.web.services.data.PeerService;
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
public class PeersControllerImpl implements PeersController {

    private final PeerService peerService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    PeersControllerImpl(PeerService peerService,
                        ProcedureService procedureService,
                        @Value("${import.path}") String importPath,
                        @Value("${export.path}") String exportPath,
                        @Value("${export.filename}") String exportFilename) {
        this.peerService = peerService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/peers")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<PeerEntity> list = this.peerService.findAll();
            model.addAttribute("peerList", list);
            log.info("Пользователь успешно получил главную страницу таблицы Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Peers. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/peers";
    }

    @Override
    @PostMapping("/addPeer")
    public String add(PeerEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.peerService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Peers. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:peers";
    }

    @Override
    @GetMapping("/findPeer")
    public String findById(@RequestParam(value = "id", required = false) String id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<PeerEntity> byId = this.peerService.findById(id);
            model.addAttribute("peerList", byId);
            log.info("Пользователь успешно нашел запись по nickname в таблице Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по nickname в таблице Peers. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/peers";
    }

    @Override
    @PostMapping("/updatePeer")
    public String update(PeerEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.peerService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Peers. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:peers";
    }

    @Override
    @PostMapping("/deletePeer")
    public String delete(@RequestParam(value = "id", required = false) String id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.peerService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Peers. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:peers";
    }

    @Override
    @PostMapping("/importPeers")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Peers");
            return "redirect:/error";
        }
        return "redirect:peers";
    }

    @Override
    @GetMapping("/exportPeers")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Peers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Peers");
            return "redirect:/error";
        }
        return null;
    }
}
