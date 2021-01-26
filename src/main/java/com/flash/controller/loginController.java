package com.flash.controller;

import cn.hutool.crypto.SecureUtil;
import com.flash.entity.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class loginController {
  @RequestMapping("/login")
  public String login(){
    return "login";
  }
  @RequestMapping("/tologin")
  public  String tologin(String username, String password,boolean rememberMe, Model model, HttpSession session){
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(username, SecureUtil.md5(password),rememberMe);
    try {
      subject.login(token);//执行登陆的方法
      Admin admin = (Admin)subject.getPrincipal();
      session.setAttribute("admin",admin);
      return "redirect:/manage";
    }catch (UnknownAccountException e){//用户名错误
      model.addAttribute("msg","用户名错误");
      return "login";
    }catch (IncorrectCredentialsException e){//密码错误
      model.addAttribute("msg","密码错误");
      return "login";
    }

  }

}
