package com.flash.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

  @ConfigurationProperties(prefix = "spring.datasource")
  @Bean
  public DataSource druidDataSource(){
    return new DruidDataSource();
  }
  //后台监控
  @Bean
  public ServletRegistrationBean statViewServlet(){
    ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");

    HashMap<String , String > initParam = new HashMap<>();
    //登陆的账号密码
    initParam.put("loginUsername","cdj990918");
    initParam.put("loginPassword","cdj86003382pyc");


    //允许谁可以访问
    initParam.put("allow","");//允许所有人访问


    bean.setInitParameters(initParam);
    return bean;
  }
}

