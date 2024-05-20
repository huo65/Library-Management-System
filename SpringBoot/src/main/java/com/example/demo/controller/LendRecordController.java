package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.LendRecord;
import com.example.demo.entity.User;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.LendRecordMapper;
import com.example.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/{isbn}")
    public Result<?> delete(@PathVariable String isbn){
        Map<String,Object> map = new HashMap<>();
        map.put("isbn",isbn);
        LendRecordMapper.deleteByMap(map);
        return Result.success();
    }
    //删除一条记录(no 是还书
    @PostMapping("/deleteRecord")
    public  Result<?> deleteRecord(@RequestBody LendRecord lendRecord){
        log.info("########################接受参数"+lendRecord);
        LambdaQueryWrapper<LendRecord> lendRecordLambdaQueryWrapper = Wrappers.lambdaQuery();
        lendRecordLambdaQueryWrapper.eq(LendRecord::getIsbn,lendRecord.getIsbn());
        lendRecordLambdaQueryWrapper.eq(LendRecord::getReaderId,lendRecord.getReaderId());
        lendRecordLambdaQueryWrapper.eq(LendRecord::getLendTime,lendRecord.getLendTime());
        LendRecord record = LendRecordMapper.selectOne(lendRecordLambdaQueryWrapper);
        record.setStatus("1");
        record.setReturnTime(new Date());
        LendRecordMapper.updateById(record);

        LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
        bUwrapper.eq(BookWithUser::getIsbn,lendRecord.getIsbn());
        bUwrapper.eq(BookWithUser::getLendtime,lendRecord.getLendTime());
        BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
        bU.setStatus(1);
        bookWithUserMapper.updateById(bU);

        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,lendRecord.getIsbn());
        Book book = bookMapper.selectOne(wrapper);
        book.setLeftNumber(book.getLeftNumber()+1);
        bookMapper.updateById(book);
        return Result.success();
    }
    @PostMapping("/deleteRecords")
    public Result<?> deleteRecords(@RequestBody List<LendRecord> LendRecords){
//        for (LendRecord curRecord : LendRecords) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("isbn", curRecord.getIsbn());
//            map.put("borrownum", curRecord.getBorrownum());
//            LendRecordMapper.deleteByMap(map);
//        }
        return Result.success();
    }
    @PostMapping
    public Result<?> save(@RequestBody LendRecord LendRecord){
        LendRecordMapper.insert(LendRecord);
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

    @PutMapping("/{isbn}")
    public  Result<?> update(@PathVariable String isbn,@RequestBody LendRecord lendRecord){
        UpdateWrapper<LendRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("isbn",isbn);
        LendRecord lendrecord = new LendRecord();
        lendrecord.setLendTime(lendRecord.getLendTime());
        lendrecord.setReturnTime(lendRecord.getReturnTime());
        lendrecord.setStatus(lendRecord.getStatus());
        LendRecordMapper.update(lendrecord, updateWrapper);
        return Result.success();
    }
    @PutMapping("/{lendTime}")
    public  Result<?> update2(@PathVariable Date lendTime, @RequestBody LendRecord lendRecord){
        UpdateWrapper<LendRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("lendTime",lendTime);
        LendRecord lendrecord = new LendRecord();
        lendrecord.setReturnTime(lendRecord.getReturnTime());
        lendrecord.setStatus(lendRecord.getStatus());
        LendRecordMapper.update(lendrecord, updateWrapper);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody LendRecord lendRecord){
        log.info("########################接受参数"+lendRecord);
        LendRecordMapper.updateById(lendRecord);
        if (lendRecord.getStatus() .equals("1")) {
            LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
            bUwrapper.eq(BookWithUser::getIsbn, lendRecord.getIsbn());
            bUwrapper.eq(BookWithUser::getLendtime, lendRecord.getLendTime());
            BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
            bU.setStatus(1);
            bookWithUserMapper.updateById(bU);

            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn, lendRecord.getIsbn());
            Book book = bookMapper.selectOne(wrapper);
            book.setLeftNumber(book.getLeftNumber() + 1);
            bookMapper.updateById(book);
        }else{
            LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
            bUwrapper.eq(BookWithUser::getIsbn, lendRecord.getIsbn());
            bUwrapper.eq(BookWithUser::getLendtime, lendRecord.getLendTime());
            BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
            bU.setStatus(0);
            bookWithUserMapper.updateById(bU);

            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn, lendRecord.getIsbn());
            Book book = bookMapper.selectOne(wrapper);
            book.setLeftNumber(book.getLeftNumber() - 1);
            bookMapper.updateById(book);
        }
        return Result.success();
    }
}
