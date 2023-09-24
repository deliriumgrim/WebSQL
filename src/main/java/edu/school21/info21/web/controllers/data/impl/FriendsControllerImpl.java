package edu.school21.info21.web.controllers.data.impl;

import edu.school21.info21.web.controllers.data.FriendController;
import edu.school21.info21.web.models.data.FriendEntity;
import edu.school21.info21.web.services.data.FriendService;
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
public class FriendsControllerImpl implements FriendController {

    private final FriendService friendService;
    private final ProcedureService procedureService;
    private final String importPath;
    private final String exportPath;

    @Autowired
    public FriendsControllerImpl(FriendService friendService,
                                 ProcedureService procedureService,
                                 @Value("${import.path}") String importPath,
                                 @Value("${export.path}") String exportPath,
                                 @Value("${export.filename}") String exportFilename) {
        this.friendService = friendService;
        this.procedureService = procedureService;
        this.importPath = importPath;
        this.exportPath = exportPath + exportFilename;
    }

    @Override
    @GetMapping("/friends")
    public String mainPage(Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<FriendEntity> list = this.friendService.findAll();
            model.addAttribute("friendList", list);
            log.info("Пользователь успешно получил главную страницу таблицы Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог получить главную страницу таблицы Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/friends";
    }

    @Override
    @PostMapping("/addFriend")
    public String add(FriendEntity entity,
                      RedirectAttributes redirectAttributes) {
        try {
            this.friendService.add(entity);
            log.info("Пользователь успешно добавил запись в таблицу Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог добавить запись в таблицу Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:friends";
    }

    @Override
    @GetMapping("/findFriend")
    public String findById(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            List<FriendEntity> byId = this.friendService.findById(id);
            model.addAttribute("friendList", byId);
            log.info("Пользователь успешно нашел запись по ID в таблице Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог найти запись по ID в таблице Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "data/tables/friends";
    }

    @Override
    @PostMapping("/updateFriend")
    public String update(FriendEntity entity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.friendService.update(entity);
            log.info("Пользователь успешно обновил запись в таблице Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог обновить запись в таблице Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:friends";
    }

    @Override
    @PostMapping("/deleteFriend")
    public String delete(@RequestParam(value = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        try {
            this.friendService.delete(id);
            log.info("Пользователь успешно удалил запись из таблицы Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.warn("Пользователь не смог удалить запись из таблицы Friends. Причина: " + e.getMessage());
            return "redirect:/error";
        }
        return "redirect:friends";
    }

    @Override
    @PostMapping("/importFriends")
    public String importData(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            this.procedureService.importData(multipartFile,
                    tableName,
                    this.importPath + multipartFile.getOriginalFilename());
            log.info("Пользователь успешно импортировал данные в таблицу Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог импортировать данные в таблицу Friends");
            return "redirect:/error";
        }
        return "redirect:friends";
    }

    @Override
    @GetMapping("/exportFriends")
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
            log.info("Пользователь успешно экспортировал данные из таблицы Friends");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
            log.info("Пользователь не смог экспортировать данные из таблицы Friends");
            return "redirect:/error";
        }
        return null;
    }
}
