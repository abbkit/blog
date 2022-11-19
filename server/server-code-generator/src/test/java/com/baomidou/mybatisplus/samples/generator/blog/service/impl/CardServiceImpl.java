package com.baomidou.mybatisplus.samples.generator.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.samples.generator.blog.mapper.CardMapper;
import com.baomidou.mybatisplus.samples.generator.blog.po.CardEntity;
import com.baomidou.mybatisplus.samples.generator.blog.service.CardService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author J
 * @since 2022-11-19
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, CardEntity> implements CardService {

}
