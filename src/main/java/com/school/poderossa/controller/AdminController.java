package com.school.poderossa.controller;

import com.school.poderossa.bean.PostBean;
import com.school.poderossa.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Controller
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
            return "redirect:/posts";
        } else {
            return "redirect:/editpost/" + newPostId;
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
                          @RequestParam("postId") String postId) {
        String[] html = bodyContent.split("</div>");
        String content = html[0];
        content = content.replace("<div class=\"ql-editor\" data-gramm=\"false\" contenteditable=\"true\">", "");
        // TODO
        String postPhoto = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String updateTimeStamp = now.format(formatter);

        if (Objects.equals(service.updatePost(postId, postTitle, content, postPhoto, updateTimeStamp), 1)) {
            PostBean post = service.getPost(postId);
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("savedTime", "Guardado! / 保存しました！ " + updateTimeStamp);
        } else {
            PostBean post = service.getPost(postId);
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("savedTime", "Fallo al guardar! / 保存に失敗しました！");
        }
        return "redirect:/editpage";
    }

    @RequestMapping(method = RequestMethod.GET, value="/publish/{postId}/{publicar}")
    public String publish(Model model, @PathVariable("postId") String postId, @PathVariable("publicar") Boolean publicar) {
        service.setStatus(postId, publicar);
        return "redirect:/posts";
    }

    @RequestMapping(method = RequestMethod.GET, value="/pin/{postId}/{ancla}")
    public String pin(Model model, @PathVariable("postId") String postId, @PathVariable("ancla") Boolean pin) {
        service.setPin(postId, pin);
        return "redirect:/posts";
    }

    @RequestMapping(method = RequestMethod.GET, value="/delete/{postId}")
    public String delete(Model model, @PathVariable("postId") String postId) {
        System.out.println(postId);
        return "redirect:/posts";
    }
}
