package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员视图
 * - 手动还书
 * - 修改记录
 */
@RestController
@RequestMapping("/LendRecord")
@Slf4j
public class LendRecordController {
    @Resource
    LendRecordMapper LendRecordMapper;

    @Resource
    BookWithUserMapper bookWithUserMapper;
    @Resource
    UserMapper userMapper;

    @Resource
    BookMapper bookMapper;

    @Resource
    SpecificbookMapper specificbookMapper;

    //还书接口，尽量管理端借书记录页面不变
    @PostMapping("/deleteRecord")
    public  Result<?> returnBook(@RequestBody LendRecord lendRecord){
        log.info("########################接受参数"+lendRecord);
//        记录表
        LambdaQueryWrapper<LendRecord> lendRecordLambdaQueryWrapper = Wrappers.lambdaQuery();
        lendRecordLambdaQueryWrapper.eq(LendRecord::getId,lendRecord.getId());
        LendRecord record = LendRecordMapper.selectOne(lendRecordLambdaQueryWrapper);
        record.setStatus("1");
        record.setReturnTime(new Date());
        LendRecordMapper.updateById(record);
//        用户记录表,使用bookid 和 readerid 借用时间确定记录
        LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
        bUwrapper.eq(BookWithUser::getBookId,lendRecord.getBookId());
        bUwrapper.eq(BookWithUser::getReaderId,lendRecord.getReaderId());
        bUwrapper.eq(BookWithUser::getLendtime,lendRecord.getLendTime());
        BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
        bU.setStatus(1);
        bookWithUserMapper.updateById(bU);
//      图书表
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,lendRecord.getIsbn());
        Book book = bookMapper.selectOne(wrapper);
        book.setLeftNumber(book.getLeftNumber()+1);
        bookMapper.updateById(book);
//        具体图书
        Specificbook specificbook = specificbookMapper.selectById(lendRecord.getBookId());
        specificbook.setStatus("1");
        return Result.success();
    }
    @PostMapping("/deleteRecords")
    public Result<?> deleteRecords(@RequestBody List<LendRecord> LendRecords){
        return Result.success();
    }
//    借书
    @PostMapping
    public Result<?> lendBook(@RequestBody LendRecord lendRecord){
        LendRecordMapper.insert(lendRecord);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<LendRecord> wrappers = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(LendRecord::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(LendRecord::getBookname,search2);
        }
        if(StringUtils.isNotBlank(search3)){
            wrappers.like(LendRecord::getReaderId,search3);
        }
        wrappers.orderByDesc(LendRecord::getLendTime);    //按照借阅时间最新在前排序
        Page<LendRecord> lendRecordPage =LendRecordMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        // 遍历结果，查询并填充readerName
        List<LendRecord> records = lendRecordPage.getRecords();
        for (LendRecord record : records) {
            User user = userMapper.selectById(record.getReaderId());
            if (user != null) {
                record.setReadername(user.getNickName()); // 假设LendRecord类中已有setter方法
            }
        }
        lendRecordPage.setRecords(records);

        return Result.success(lendRecordPage);
    }

    @PutMapping
    public  Result<?> update(@RequestBody LendRecord lendRecord){
        lendRecord.setReturnTime(null);
        log.info("########################接受参数"+lendRecord);
        LendRecordMapper.updateById(lendRecord);
        if (lendRecord.getStatus() .equals("1")) {
            LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
            bUwrapper.eq(BookWithUser::getBookId, lendRecord.getBookId());
            bUwrapper.eq(BookWithUser::getReaderId,lendRecord.getReaderId());
            bUwrapper.eq(BookWithUser::getStatus,"0");
            BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
            bU.setStatus(1);
            bookWithUserMapper.updateById(bU);

            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn, lendRecord.getIsbn());
            Book book = bookMapper.selectOne(wrapper);
            book.setLeftNumber(book.getLeftNumber() + 1);
            bookMapper.updateById(book);

            Specificbook specificbook = specificbookMapper.selectById(lendRecord.getBookId());
            specificbook.setStatus("1");

        }else{
            LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
            bUwrapper.eq(BookWithUser::getBookId, lendRecord.getBookId());
            bUwrapper.eq(BookWithUser::getReaderId,lendRecord.getReaderId());
            bUwrapper.eq(BookWithUser::getStatus,"1");
            BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
            bU.setStatus(0);
            bookWithUserMapper.updateById(bU);

            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn, lendRecord.getIsbn());
            Book book = bookMapper.selectOne(wrapper);
            book.setLeftNumber(book.getLeftNumber() - 1);
            bookMapper.updateById(book);
            Specificbook specificbook = specificbookMapper.selectById(lendRecord.getBookId());
            specificbook.setStatus("0");
        }
        return Result.success();
    }
}
