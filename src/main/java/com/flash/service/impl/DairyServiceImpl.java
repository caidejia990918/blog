package com.flash.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.entity.Admin;
import com.flash.entity.Dairy;
import com.flash.mapper.AdminMapper;
import com.flash.mapper.DairyMapper;
import com.flash.service.AdminService;
import com.flash.service.DairyService;
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
public class DairyServiceImpl extends ServiceImpl<DairyMapper, Dairy> implements DairyService {

}
