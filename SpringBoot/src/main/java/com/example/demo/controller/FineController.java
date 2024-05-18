package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Fine;
import com.example.demo.mapper.FineMapper;
import com.example.demo.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huo
 * @date 2024/05/18 19:58
 **/
@RestController
@RequestMapping("/fine")
public class FineController {
    @Autowired
    FineMapper fineMapper;
    @Autowired
    FineService fineService;

    @PostMapping("/add")
    public Result<?> insertNew(@RequestBody List<Fine> fineList){
        System.out.println("@@@@"+fineList);
        fineService.batchInsertFine(fineList);
        return Result.success();
    }
    @PostMapping("/update")
    public Result<?> update(@RequestBody Fine fine){
        UpdateWrapper<Fine> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",fine.getId());
        fineMapper.update(fine, updateWrapper);
        return Result.success();
    }

    @PostMapping("/deleteRecord")
    @Transactional
    public  Result<?> deleteRecord(@RequestBody Fine bookWithUser){

        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<Fine> wrappers = Wrappers.<Fine>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(Fine::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(Fine::getReadername,search2);
        }

        Page<Fine> FinePage =fineMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(FinePage);
    }
    
}
