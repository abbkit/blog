package com.abbkit.project.blog.card.controller;

import com.abbkit.project.model.ResponseModel;
import com.abbkit.project.model.SimplePageRequest;
import com.abbkit.project.blog.BlogCons;
import com.abbkit.project.blog.card.po.CardEntity;
import com.abbkit.project.blog.card.service.CardService;
import com.abbkit.project.blog.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author J
 * @since 2022-11-19
 */
@RestController
@RequestMapping(BlogCons.PATH_PREFIX+"/cardEntity")
public class CardController {


    @Autowired
    private CardService cardService;

    @ResponseBody
    @RequestMapping(path="/insert",method= RequestMethod.POST)
    public ResponseModel save(CardEntity record) throws Exception{
        cardService.save( record);
        return ResponseModel.newSuccess(record.getId());
    }

    @ResponseBody
    @RequestMapping(path="/update",method=RequestMethod.POST)
    public ResponseModel update( CardEntity record) throws Exception{
        cardService.updateById(record);
        return ResponseModel.newSuccess(true);
    }


    @ResponseBody
    @RequestMapping(path="/delete",method=RequestMethod.POST)
    public ResponseModel delete( String id) throws Exception{
        cardService.removeById(id);
        return ResponseModel.newSuccess(true);
    }


    @ResponseBody
    @RequestMapping(path="/get",method=RequestMethod.GET)
    public ResponseModel get( Long id) throws Exception{
        CardEntity record= cardService.getById( id);
        return ResponseModel.newSuccess(record);
    }

    @ResponseBody
    @RequestMapping(value="/page",method= RequestMethod.POST)
    public ResponseModel page(CardEntity record
            , SimplePageRequest simplePageRequest) throws Exception{
        Page<CardEntity> objectPage = Page.of(simplePageRequest.pageNumber(), simplePageRequest.pageSize());
        QueryWrapper<CardEntity> entityQueryWrapper = new QueryWrapper<>();
        Page<CardEntity> pageResult= cardService.page(objectPage, entityQueryWrapper);
        return ResponseModel.newSuccess(PageUtil.of(pageResult,simplePageRequest));
    }

    @ResponseBody
    @RequestMapping(value="/list",method= RequestMethod.POST)
    public ResponseModel list(CardEntity record) throws Exception{
        List<CardEntity> list= cardService.list();
        return ResponseModel.newSuccess(list);
    }

}
