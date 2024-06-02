package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.dto.SuggestPageDto;
import com.example.demo.entity.Suggest;
import com.example.demo.mapper.SuggestMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fyftless
 */
@RestController
@RequestMapping("/suggest")
public class ApiSuggestController {

    @Resource
    private SuggestMapper suggestMapper;

    /**
     * 新增留言
     * @param suggest 留言
     * @return 返回信息
     */
    @PostMapping
    public Result<?> save(@RequestBody Suggest suggest){
        suggestMapper.insert(suggest);
        return Result.success();
    }

    /**
     * 更新留言
     * @param suggest 留言
     * @return 返回信息
     */
    @PutMapping
    public Result<?> update(@RequestBody Suggest suggest){
        suggestMapper.updateById(suggest);
        return Result.success();
    }

    /**
     * 批量删除
     * @param ids id集合
     * @return 返回信息
     */
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        suggestMapper.deleteBatchIds(ids);
        return Result.success();
    }

    /**
     * 分页查询
     * @param suggestPageDto 查询条件
     * @return 分页信息
     */
    @GetMapping
    public Result<?> findPage(@RequestBody SuggestPageDto suggestPageDto){
        LambdaQueryWrapper<Suggest> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(suggestPageDto.getNickName())){
            queryWrapper.like(Suggest::getNickName, suggestPageDto.getNickName());
        }
        if (StringUtils.isNotBlank(suggestPageDto.getReplyName())){
            queryWrapper.like(Suggest::getReplyName, suggestPageDto.getReplyName());
        }
        if (suggestPageDto.getReplied() != null){
            queryWrapper.eq(Suggest::getReplied, suggestPageDto.getReplied());
        }
        queryWrapper.orderByDesc(Suggest::getCreateTime);
        Page<Suggest> page = suggestMapper.selectPage(new Page<>(suggestPageDto.getPageNum(), suggestPageDto.getPageSize()), queryWrapper);
        return Result.success(page);
    }
}
