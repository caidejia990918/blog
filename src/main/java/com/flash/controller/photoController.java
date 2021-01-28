package com.flash.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flash.entity.Photo;
import com.flash.service.PhotoService;
import com.flash.util.ImageUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

@Controller
public class photoController {

  @Autowired
  PhotoService photoService;
  private String path = "/Users/cdj990918/Downloads/photo/";
  private String path2 = "/Users/cdj990918/Downloads/flash/";
//  private String path = "/root/photo/";
//  private String path2 = "/root/flash/";





  @RequestMapping("/manage/addPhoto")
  public String addPhoto(){
    return "photoManage/addPhoto";
  }

  @RequestMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("category") String name , Model model) throws SQLIntegrityConstraintViolationException {
    if (!file.isEmpty()) {
      try {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
          extension = originalFilename.substring(i+1);
        }
        if(ImageUtil.isImage(extension)){

        }
        else{
          model.addAttribute("msg","上传失败，请上传图片");
          return "photoManage/addPhoto";
        }

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path+file.getOriginalFilename())));
        System.out.println("上传成功");
        out.write(file.getBytes());
        out.flush();
        out.close();

        //String pp = "/Users/cdj990918/Downloads/photo/";
        String [] files = new String[]{
          path+file.getOriginalFilename()
        };
        ImageUtil.generateThumbnail2Directory(path2,files);
        files = null;

      } catch (FileNotFoundException e) {
        e.printStackTrace();

        model.addAttribute("msg", "上传失败," + e.getMessage());
        return "photoManage/addPhoto";
      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("msg","上传失败," + e.getMessage());
        return "photoManage/addPhoto";
      }

      Photo photo = new Photo();
      photo.setCategory(name);
      String temp =file.getOriginalFilename();
      photo.setName(temp);
      StringBuffer ss = new StringBuffer(temp).insert(temp.indexOf("."), "-thumbnail");
      photo.setNamef(ss.toString());

      try {
        photoService.save(photo);
      } catch (DataIntegrityViolationException e) {
        model.addAttribute("msg","上传失败，照片已存在");
      }finally {
        model.addAttribute("msg","上传成功");
        return "photoManage/addPhoto";
      }



    } else {
      model.addAttribute("msg", "上传失败，因为文件是空的.");
      return "photoManage/addPhoto";
    }
  }
  @RequestMapping("/manage/show")
  public String showPhotos(Model model){
    List<Photo> Photos = photoService.list(new QueryWrapper<Photo>());
//    String path = "/Users/cdj990918/Downloads/Photo/";
//    File[] files = new File(path).listFiles();
    model.addAttribute("photos", Photos);
    return "photoManage/showPhotos";
  }
@RequestMapping("/manage/show/delete/{id}")
  public String deletePhoto(@PathVariable("id")Long id){
  QueryWrapper<Photo> photo = new QueryWrapper<Photo>().eq("id", id);
  Photo one = photoService.getOne(photo);
  photoService.remove(photo);
  File file = new File(path + one.getName());
  File file1 = new File(path2+one.getNamef());
  if(file.delete()&&file1.delete()){
    System.out.println("删除成功");
  }
  else
  {
    System.out.println("删除失败");
  }
  return  "redirect:/manage/show";
  }

  @RequestMapping("/manage/show/edit/{id}")
  public String editPhoto(@PathVariable("id")Long id,@RequestParam("category")String name){
    QueryWrapper<Photo> photo = new QueryWrapper<Photo>().eq("id", id);
    Photo one = photoService.getOne(photo);
    one.setCategory(name);
    photoService.saveOrUpdate(one);
    return  "redirect:/manage/show";
  }

  @RequestMapping("/")
  public String getPhotos(Model model){
    List<Photo> Photos = photoService.list(new QueryWrapper<Photo>());
    model.addAttribute("photos", Photos);
    List<Integer> random = getRandom(Photos.size());
    model.addAttribute("random",random);
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


  public  List<Integer> getRandom(int length){
    int size = min(length,15);
    List mylist = new ArrayList<Integer>(); //生成数据集，用来保存随即生成数，并用于判断
    Random rd = new Random();
    while(mylist.size() < size) {
      int num = rd.nextInt(length);
      if(!mylist.contains(num)) {
        mylist.add(num); //往集合里面添加数据。
      }
    }

    return mylist;
  }
}
