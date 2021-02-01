package com.flash.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //将所有/static/** 访问都映射到classpath:/static/ 目录下

//addResourceLocations的每一个值必须以'/'结尾,否则虽然映射了,但是依然无法访问该目录下的的文件(支持: classpath:/xxx/xx/, file:/xxx/xx/, http://xxx/xx/)
//    registry.addResourceHandler("/photos/**").addResourceLocations("file:/Users/cdj990918/Downloads/photo/");
//    registry.addResourceHandler("/photosf/**").addResourceLocations("file:/Users/cdj990918/Downloads/flash/");
//    registry.addResourceHandler("/article/img/**").addResourceLocations("file:/Users/cdj990918/Downloads/blogimg/");
    registry.addResourceHandler("/photos/**").addResourceLocations("file:/root/photo/");
    registry.addResourceHandler("/photosf/**").addResourceLocations("file:/root/flash/");
    registry.addResourceHandler("/article/img/**").addResourceLocations("file:/root/blogimg/");

  }

}
