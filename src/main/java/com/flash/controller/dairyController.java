package com.flash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class dairyController {

  @RequestMapping("/dairy")
  public String dairy()
  {
    return "dairy";
  }
  @RequestMapping("/dairy/edit")
  public String editDairy(){
    return "edit";
  }

  @RequestMapping("/dairy/edit/submit")
  public String submitDairy(@RequestParam("title")String title,@RequestParam("description")String description,@RequestParam("content")String content){
    System.out.println(content);
    return "redirect:/dairy";
  }
}
