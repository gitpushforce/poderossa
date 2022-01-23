package com.school.poderossa.controller;

import com.school.poderossa.api.DriveQuickstart;
import com.school.poderossa.bean.PostBean;
import com.school.poderossa.bean.ScheduleBean;
import com.school.poderossa.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @RequestMapping(method = RequestMethod.GET, value="/posts")
    public String posts(Model model) {

        List<PostBean> posts = service.getPosts();
        model.addAttribute("posts", posts);

        return "admin/post-list";
    }

    @RequestMapping(method = RequestMethod.GET, value="/editpage")
    public String editPage(Model model, RedirectAttributes redirectAttributes) {
        return "admin/create-edit-post";
    }

    @RequestMapping(method = RequestMethod.GET, value="/createpost")
    public String createPost(Model model, RedirectAttributes redirectAttributes) {
        String profeId = "222333";
        String newPostId = service.createNewPost(profeId);
        if (newPostId.isEmpty()) {
            redirectAttributes.addFlashAttribute("createNewPostErrorMsg", "no se pudo crear");
            return "redirect:/admin/posts";
        } else {
            return "redirect:/admin/editpost/" + newPostId;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="/editpost/{postId}")
    public String editPost(Model model, RedirectAttributes redirectAttributes, @PathVariable("postId") String postId) {
        PostBean post = service.getPost(postId);
        model.addAttribute("post", post);
        return "admin/create-edit-post";
    }

    @RequestMapping(method=RequestMethod.POST, value="/guardar")
    public String guardar(Model model, RedirectAttributes redirectAttributes, @RequestParam("datosEditor") String bodyContent, @RequestParam("postTitle") String postTitle,
                          @RequestParam("postId") String postId, @RequestParam("photo") MultipartFile photo, @RequestParam("oldPhoto") String oldPhoto) throws GeneralSecurityException, IOException {
        String[] html = bodyContent.split("</div>");
        String content = html[0];
        content = content.replace("<div class=\"ql-editor\" data-gramm=\"false\" contenteditable=\"true\">", "");

        String postPhoto="";
        if (photo.isEmpty()) {
            postPhoto = oldPhoto;
        } else {
            // converting Multipart to File (need to save it in resources directory)
            File file = new File("src/main/resources/" + photo.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(photo.getBytes());
            }
            // upload the file to google drive and get the Id
            postPhoto = DriveQuickstart.getGoogleDriveUploadedFileId(file, "posts");
            // delete file in resources when its uploaded in google drive already
            if (!postPhoto.isEmpty()) {
                file.delete();
            }
            // TODO escribir codigo para borrar foto que ya no se usa en google drive
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String updateTimeStamp = now.format(formatter);

        // update
        if (Objects.equals(service.updatePost(postId, postTitle, content, postPhoto, updateTimeStamp), 1)) {
            PostBean post = service.getPost(postId);
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("savedTime", "Guardado! / 保存しました！ " + updateTimeStamp);
        } else {
            PostBean post = service.getPost(postId);
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("savedTime", "Fallo al guardar! / 保存に失敗しました！");
        }
        return "redirect:/admin/editpage";
    }

    @RequestMapping(method = RequestMethod.GET, value="/publish/{postId}/{publicar}")
    public String publish(Model model, @PathVariable("postId") String postId, @PathVariable("publicar") Boolean publicar) {
        service.setStatus(postId, publicar);
        return "redirect:/admin/posts";
    }

    @RequestMapping(method = RequestMethod.GET, value="/pin/{postId}/{ancla}")
    public String pin(Model model, @PathVariable("postId") String postId, @PathVariable("ancla") Boolean pin) {
        service.setPin(postId, pin);
        return "redirect:/admin/posts";
    }

    @RequestMapping(method = RequestMethod.GET, value="/delete/{postId}")
    public String delete(Model model, @PathVariable("postId") String postId) {
        System.out.println(postId);
        return "redirect:/admin/posts";
    }

    @RequestMapping(method = RequestMethod.GET, value="/schedule")
    public String schedule(Model model) {
//        System.out.println(postId);
        List<ScheduleBean> scheduleList = service.getScheduleList();
//        ScheduleBean schedule = new ScheduleBean();
//        schedule.setEventId("234234");
//        schedule.setEventName("eventooooo");
//        schedule.setEventPlace("en la cancha");
//        schedule.setEventDate("2024年12月12日");
//        schedule.setEventTime("12:23 am");
//
//        ScheduleBean schedule2 = new ScheduleBean();
//        schedule2.setEventId("4444");
//        schedule2.setEventName("ggfgdgf");
//        schedule2.setEventPlace("en la 4444");
//        schedule2.setEventDate("2024年11月12日");
//        schedule2.setEventTime("12:13 am");



        model.addAttribute("scheduleList", scheduleList);
        return "admin/admin-calendar";
    }

    @RequestMapping(method = RequestMethod.POST, value="/set-schedule")
    public String setSchedule(Model model, @RequestParam("evento") String evento,
                              @RequestParam("lugar") String lugar, @RequestParam("fecha") String fecha,
                              @RequestParam("hora") String hora) throws ParseException {

        service.addSchedule(evento, lugar, fecha, hora);
        return "redirect:/admin/schedule";
    }

    @RequestMapping(method = RequestMethod.GET, value="/delete-schedule/{id}")
    public String deleteSchedule(Model model, @PathVariable("id") String eventId) {

        service.deleteSchedule(eventId);
        return "redirect:/admin/schedule";
    }

}
