package com.flash.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    //3
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getdefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);


//        anon:无需认证即可访问
//        authc:必须认证了才能访问
//        user:必须使用记住我功能才能使用
//        perms:拥有对某个资源的权限才能访问
//        role:拥有某个角色权才能访问

        LinkedHashMap<String,String> filtermap = new LinkedHashMap<>();

          filtermap.put("/manage/**","user");
          filtermap.put("/css/**","anon");
          bean.setFilterChainDefinitionMap(filtermap);
          bean.setLoginUrl("/login");//当访问权限范围外的页面时 会自动跳转到改页面
//        bean.setUnauthorizedUrl("/unauth");
        return bean;
    }

    //2
    @Bean
    public DefaultWebSecurityManager getdefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setSessionManager(mySessionManager());
        return defaultWebSecurityManager;
    }


    //创建Realm对象 1
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }


    //整合ShiroDialect
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }




  @Bean
  public DefaultWebSessionManager mySessionManager(){
    DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
    //将sessionIdUrlRewritingEnabled属性设置成false
//    defaultSessionManager.setGlobalSessionTimeout(6000L);
    defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
    return defaultSessionManager;
  }

    @Bean
    public SimpleCookie rememberMeCookie() {
      //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
      SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
      //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

      //setcookie()的第七个参数
      //设为true后，只能通过http访问，javascript无法访问
      //防止xss读取cookie
      simpleCookie.setHttpOnly(true);
      //simpleCookie.setPath("/manage");
      //<!-- 记住我cookie生效时间30天 ,单位秒;-->
      simpleCookie.setMaxAge(86400);
      return simpleCookie;
    }

  @Bean
  public CookieRememberMeManager rememberMeManager(){
    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    cookieRememberMeManager.setCookie(rememberMeCookie());
    //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
    cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
    return cookieRememberMeManager;
  }



  @Bean
  public FormAuthenticationFilter formAuthenticationFilter(){
    FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
    //对应前端的checkbox的name = rememberMe
    formAuthenticationFilter.setRememberMeParam("rememberMe");
    return formAuthenticationFilter;
  }

  @Bean
  public DefaultSecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    // 设置realm
    securityManager.setRealm(userRealm());
    // 用户授权/认证信息Cache, 采用EhC//注入记住我管理器
    securityManager.setRememberMeManager(rememberMeManager());
    return securityManager;
  }

}
