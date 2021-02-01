package com.flash.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.entity.Blog;
import com.flash.entity.Photo;
import com.flash.mapper.BlogMapper;
import com.flash.mapper.PhotoMapper;
import com.flash.service.BlogService;
import com.flash.service.PhotoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
