package com.flash.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flash.entity.Photo;
import com.flash.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Controller
public class photoController {

  @Autowired
  PhotoService photoService;
  private String path = "/Users/cdj990918/Downloads/Photo/";


  @RequestMapping("/manage")
  public String PhotoManage(){
    return "manage";
  }

  @RequestMapping("addPhoto")
  public String addPhoto(){
    return "addPhoto";
  }

  @RequestMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("category") String name ,Model model) throws SQLIntegrityConstraintViolationException {
    if (!file.isEmpty()) {
      try {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
          extension = originalFilename.substring(i+1);
        }
        if(("jpg".equals(extension))||("jpeg".equals(extension))){

        }
        else{
          model.addAttribute("msg","上传失败，请传入后缀名为jpg或者jpeg的图片");
          return "addPhoto";
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path+file.getOriginalFilename())));
        System.out.println(file.getName());
        out.write(file.getBytes());
        out.flush();
        out.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();

        model.addAttribute("msg", "上传失败," + e.getMessage());
        return "addPhoto";
      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("msg","上传失败," + e.getMessage());
        return "addPhoto";
      }

      Photo photo = new Photo();
      photo.setCategory(name);
      photo.setName(file.getOriginalFilename());
      try {
        photoService.save(photo);
      } catch (DataIntegrityViolationException e) {
        model.addAttribute("msg","上传失败，照片已存在");
      }finally {
        model.addAttribute("msg","上传成功");
      }




      return "addPhoto";

    } else {
      model.addAttribute("msg", "上传失败，因为文件是空的.");
      return "addPhoto";
    }
  }
  @RequestMapping("/show")
  public String showPhotos(Model model){
    List<Photo> Photos = photoService.list(new QueryWrapper<Photo>());
//    String path = "/Users/cdj990918/Downloads/Photo/";
//    File[] files = new File(path).listFiles();
    model.addAttribute("photos", Photos);
    return "showPhotos";
  }
@RequestMapping("/show/delete/{id}")
  public String deleteBook(@PathVariable("id")Long id){
  QueryWrapper<Photo> photo = new QueryWrapper<Photo>().eq("id", id);
  Photo one = photoService.getOne(photo);
  photoService.remove(photo);
  File file = new File(path + one.getName());
  String s = one.getName();
  System.out.println(s);
  if(file.delete()){
    System.out.println("删除成功");
  }
  return  "redirect:/show";
  }

  @RequestMapping("/")
  public String getPhotos(Model model){
    List<Photo> Photos = photoService.list(new QueryWrapper<Photo>());
    model.addAttribute("photos", Photos);
    return "index";
  }

  @RequestMapping("/photograph")
  public String PhotoGraph(Model model){
    List<Photo> Photos = photoService.list(new QueryWrapper<Photo>());
    model.addAttribute("photos", Photos);
    return "photograph";
  }

  @RequestMapping("/gallery")
  public String Gallery(){
    return "gallery";
  }

}
