package com.flash.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.entity.Admin;
import com.flash.entity.Blog;
import com.flash.entity.Photo;
import com.flash.service.BlogService;
import com.flash.service.impl.BlogServiceImpl;
import com.flash.util.ImageUtil;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

@Controller
public class ArticleController {


//  private String path = "/Users/cdj990918/Downloads/blogimg/";
  private String path = "/root/blogimg/";
  @Autowired
  BlogService blogService;

  private int flash =1;

  @RequestMapping("/articles/{currentPage}")
  public String showBlogs(Model model,@PathVariable("currentPage")int currentPage){
    List<Blog> recommendedBlogs = blogService.list(new QueryWrapper<Blog>().orderByDesc("watch"));
    List<Blog> rblogs = new ArrayList<Blog>();;
    int cnt = 1;
    for(Blog b: recommendedBlogs){
      rblogs.add(b);
      if(++cnt>4)break;
    }
    model.addAttribute("recommendedBlogs",rblogs);
    Page blogIPage = new Page(currentPage,5);
    IPage<Blog> page = blogService.page(blogIPage, new QueryWrapper<Blog>().orderByDesc("created"));
//    for(Blog b :page.getRecords()){
//      System.out.println(b);
//    }

    model.addAttribute("page",page);
    model.addAttribute("totalPage",page.getPages());
    model.addAttribute("pageNumber",currentPage);
    return "article/showArticle";
  }

  @RequestMapping("/manage/article")
  public String articleManage(){
    return "article/articleManage";
  }

  @RequestMapping("/manage/article/add")
  public String addArticle(){
    return "article/add";
  }

  @RequestMapping("/manage/article/add/submit")
  public String SubmitArticle(@RequestParam("title")String title, @RequestParam("description")String description ,@RequestParam("content")String content ,
                              @RequestParam("category")String category,@RequestParam("text")String text ,HttpSession session){
    Admin admin = (Admin)session.getAttribute("admin");
    Blog blog = new Blog();
    blog.setCreated(LocalDateTime.now());
    blog.setComment(0L);
    blog.setWatch(0L);
    blog.setWriter(admin.getNickname());
    blog.setDescription(description);
    blog.setTitle(title);
    blog.setContent(content);
    blog.setText(text);
    blog.setCategory(category);
    flash++;
    String url = "/img/article/";
    blog.setPicture(url+(flash%6+1)+".jpg");
    blogService.saveOrUpdate(blog);
    return "redirect:/articles/1";
  }



  @RequestMapping("/article/{id}")
  public String articlePage(@PathVariable("id") Long id,Model model){
    Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("id", id));
    blog.setWatch(blog.getWatch()+1);
    blogService.saveOrUpdate(blog);
    model.addAttribute("blog",blog);
    return "article/article";
  }


  @RequestMapping("/manage/article/edit")
  public String articleEdit(Model model){
    List<Blog> list = blogService.list(new QueryWrapper<Blog>().orderByDesc("created"));
    model.addAttribute("blogs",list);
    return "article/edit";
  }

  @RequestMapping("/manage/article/delete/{id}")
  public String deleteArticle(@PathVariable("id") Long id){
    blogService.remove(new QueryWrapper<Blog>().eq("id",id));
    return "redirect:/manage/article/edit";
  }
  @RequestMapping("/manage/article/edit/{id}")
  public String getarticleEdit(Model model ,@PathVariable("id") Long id){
    Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("id", id));
    model.addAttribute("blog",blog);
    return "article/toEdit";
  }

  @RequestMapping("/manage/article/edit/submit/{id}")
  public String ToEdit(@RequestParam("title")String title, @RequestParam("description")String description ,@RequestParam("content")String content ,
                              @RequestParam("category")String category,@RequestParam("text")String text ,@PathVariable("id") Long id){
    Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("id", id));
    blog.setCreated(LocalDateTime.now());
    blog.setDescription(description);
    blog.setTitle(title);
    blog.setContent(content);
    blog.setText(text);
    blog.setCategory(category);
    blogService.saveOrUpdate(blog);
    return "redirect:/article/{id}";
  }

  @RequestMapping("/article/img/upload")
  @ResponseBody
  public JSONObject editormdPic (@RequestParam(value = "editormd-image-file", required = true) MultipartFile file) throws Exception{

    String trueFileName = file.getOriginalFilename();

    String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));

    String fileName = file.getOriginalFilename();


    File targetFile = new File(path, fileName);
    if(!targetFile.exists()){
      targetFile.mkdirs();

    }
    //保存
    try {
      file.transferTo(targetFile);
    } catch (Exception e) {
      e.printStackTrace();
    }


    JSONObject res = new JSONObject();
    res.put("url","/article/img/"+fileName);
    res.put("success", 1);
    res.put("message", "upload success!");

    return res;

  }
}

