package com.flash.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flash.entity.Dairy;
import com.flash.service.DairyService;
import com.flash.service.impl.DairyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class dairyController {


  @Autowired
  DairyService dairyService;



  @RequestMapping("/dairy")
  public String dairy(Model model)
  {
    List<Dairy> dairyList = dairyService.list(new QueryWrapper<Dairy>().orderByDesc("created"));
    List<String>dd =new ArrayList<String>();
    model.addAttribute("dairy",dairyList);
    return "dairy/dairy";
  }
  @RequestMapping("/manage/dairy")
  public String DairyManage(){
    return "dairy/dairyManage";
  }

  @RequestMapping("/manage/dairy/add")
  public String addDairy(){
    return "dairy/add";
  }

  @RequestMapping("/manage/dairy/edit")
  public String editDairy(Model model){
    List<Dairy> created = dairyService.list(new QueryWrapper<Dairy>().orderByDesc("created"));
    model.addAttribute("dairy",created);
    return "dairy/edit";
  }

  @RequestMapping("/manage/dairy/edit/{id}")
  public String ToeditDairy(Model model,@PathVariable("id")Long id){
    Dairy d = dairyService.getOne(new QueryWrapper<Dairy>().eq("id", id));
    model.addAttribute("dairy",d);
    return "dairy/toEdit";
  }

  @RequestMapping("/manage/dairy/delete/{id}")
  public String deleteDairy(Model model,@PathVariable("id")Long id){
    dairyService.remove(new QueryWrapper<Dairy>().eq("id", id));
    return "redirect:/manage/dairy/edit";
  }

  @RequestMapping("/manage/dairy/add/submit")
  public String submitDairy(@RequestParam("title")String title, @RequestParam("description")String description ,@RequestParam("date") Date date){
    Dairy dairy = new Dairy();
    dairy.setCreated(date);
    dairy.setDescription(description);
    dairy.setTitle(title);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    dairy.setFormat(dairy.getCreated().toLocalDate().format(formatter));
    dairyService.saveOrUpdate(dairy);
    return "redirect:/dairy";
  }

  @RequestMapping("/manage/dairy/edit/submit/{id}")
  public String submitDairy2(@PathVariable("id")Long id,@RequestParam("title")String title, @RequestParam("description")String description ,@RequestParam("date") Date date){
    Dairy d = dairyService.getById(id);
    d.setTitle(title);
    d.setDescription(description);
    d.setCreated(date);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    d.setFormat(d.getCreated().toLocalDate().format(formatter));
    dairyService.saveOrUpdate(d);
    return "redirect:/manage/dairy/edit";
  }
}
