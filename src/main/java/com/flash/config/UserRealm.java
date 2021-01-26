package com.flash.config;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flash.entity.Admin;
import com.flash.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Security;

public class UserRealm extends AuthorizingRealm {

    @Autowired
   AdminService adminService;


    @Override//授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前这个对象
        Subject subject = SecurityUtils.getSubject();
        Admin currentUser = (Admin) subject.getPrincipal();

        info.addStringPermission(currentUser.getPermission());


        return info;
    }

    @Override//认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证doGetAuthenticationInfo");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
      Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("name",userToken.getUsername()));

        if(admin==null)
            return null;//自动抛出UnknownAccountException异常

        Subject CurrSubject = SecurityUtils.getSubject();
        Session session = CurrSubject.getSession();
        session.setAttribute("admin",admin);

        return new SimpleAuthenticationInfo(admin, admin.getPassword(),"");//密码管理shiro来做
    }


}
