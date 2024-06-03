package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Specificbook;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.LendRecordMapper;
import com.example.demo.mapper.SpecificbookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 1. @ClassName SpecialBookController
 * 2. @Description 具体图书，统一借书逻辑
 * 3. @Author huo
 * 4. @Date 2024/6/3 17:06
 */
@RestController
@RequestMapping("/specificbook")
public class SpecialBookController {
    @Resource
    SpecificbookMapper specificbookMapper;

    @Resource
    BookMapper bookMapper;

    @PostMapping("/borrow")
    public  Result<?> update(@RequestBody Specificbook specificbook){
//        具体图书
        LambdaQueryWrapper<Specificbook> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Specificbook::getId,specificbook.getId());
        Specificbook selectOne =  specificbookMapper.selectOne(wrapper);
        if (selectOne == null){
            return Result.error("-1","借书失败01!");
        }
        selectOne.setStatus("0");
        specificbookMapper.updateById(selectOne);
//        图书种类
        LambdaQueryWrapper<Book> bookWrapper = Wrappers.lambdaQuery();
        bookWrapper.eq(Book::getIsbn,specificbook.getIsbn());
        Book selectOneBook =  bookMapper.selectOne(bookWrapper);
        if (selectOneBook == null){
            return Result.error("-1","借书失败02!");
        }
        selectOneBook.setBorrownum(selectOneBook.getBorrownum() + 1);
        selectOneBook.setLeftNumber(selectOneBook.getLeftNumber() - 1);
        bookMapper.updateById(selectOneBook);
//        插入记录，由BookWithUser，Len完成
        return Result.success();
    }
}
