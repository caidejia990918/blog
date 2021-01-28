package com.flash.controller;

import cn.hutool.crypto.SecureUtil;
import com.flash.entity.Admin;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class loginController {
  @RequestMapping("/login")
  public String login(){
    return "login";
  }

  @RequestMapping("/tologin")
  public  void tologin(String username, String password, boolean rememberMe, HttpSession session,
    HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(username, SecureUtil.md5(password),rememberMe);
    SavedRequest savedRequest= WebUtils.getSavedRequest(req);
    try {
      subject.login(token);//执行登陆的方法
      Admin admin = (Admin)subject.getPrincipal();
      session.setAttribute("admin",admin);
      String method = savedRequest.getMethod();  //GET/POST
      String requestUrl = savedRequest.getRequestUrl();
      resp.sendRedirect(requestUrl);
      return ;
//      return "redirect:/manage";
    }catch (UnknownAccountException e){//用户名错误
      session.setAttribute("msg","用户名错误");
      resp.sendRedirect("/login");
      return;
//      return "login";
    }catch (IncorrectCredentialsException e){//密码错误
      session.setAttribute("msg","密码错误");
      resp.sendRedirect("/login");
      return;
//      return "login";
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @RequestMapping("/manage")
  public String Manage(){
    return "manage";
  }
}
