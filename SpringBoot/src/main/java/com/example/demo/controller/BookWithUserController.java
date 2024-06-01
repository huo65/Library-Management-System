package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.LendRecord;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.LendRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/bookwithuser")
public class BookWithUserController {
    @Resource
    BookWithUserMapper bookWithUserMapper;

    @Resource
    BookMapper bookMapper;

    @Resource
    LendRecordMapper lendRecordMapper;


    @PostMapping("/insertNew")
    public Result<?> insertNew(@RequestBody BookWithUser BookWithUser){
        bookWithUserMapper.insert(BookWithUser);
        return Result.success();
    }
    @PostMapping
    public Result<?> update(@RequestBody BookWithUser BookWithUser){
        UpdateWrapper<BookWithUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("isbn",BookWithUser.getIsbn()).eq("nick_name",BookWithUser.getNickName());
        bookWithUserMapper.update(BookWithUser, updateWrapper);
        return Result.success();
    }
//还书
    @PostMapping("/deleteRecord")
    @Transactional
    public  Result<?> deleteRecord(@RequestBody BookWithUser bookWithUser){
        LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
        bUwrapper.eq(BookWithUser::getId,bookWithUser.getId());
        BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
        bookWithUserMapper.updateById(bU);

//        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
//        wrapper.eq(Book::getIsbn,bookWithUser.getIsbn());
//        Book book = bookMapper.selectOne(wrapper);
//        book.setLeftNumber(book.getLeftNumber()+1);
//        bookMapper.updateById(book);

        LambdaQueryWrapper<LendRecord> lendRecordLambdaQueryWrapper = Wrappers.lambdaQuery();
        lendRecordLambdaQueryWrapper.eq(LendRecord::getIsbn,bookWithUser.getIsbn());
        lendRecordLambdaQueryWrapper.eq(LendRecord::getLendTime,bookWithUser.getLendtime());
        LendRecord lendRecord = lendRecordMapper.selectOne(lendRecordLambdaQueryWrapper);
        lendRecordMapper.updateById(lendRecord);

        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<BookWithUser> wrappers = Wrappers.<BookWithUser>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(BookWithUser::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(BookWithUser::getBookName,search2);
        }
        wrappers.orderByDesc(BookWithUser::getLendtime);   //按借阅时间排序
        Page<BookWithUser> BookPage =bookWithUserMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(BookPage);
    }
}
