package com.example.demo.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.LendRecord;
import com.example.demo.entity.Specificbook;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.LendRecordMapper;
import com.example.demo.mapper.SpecificbookMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 管理员批量还书
 */
@RestController
@Slf4j
@RequestMapping("/LendRecordByScan")
public class LendRecordController1 {
    @Resource
    LendRecordMapper lendRecordMapper;
    @Resource
    BookMapper bookMapper;
    @Resource
    BookWithUserMapper bookWithUserMapper;
    @Resource
    SpecificbookMapper specificbookMapper;
    @PutMapping
    public  Result<?> update2( @RequestBody Integer lendRecordID){
        log.info("########################接受参数"+lendRecordID);
//        记录表
        LambdaQueryWrapper<LendRecord> lendRecordLambdaQueryWrapper = Wrappers.lambdaQuery();
        lendRecordLambdaQueryWrapper.eq(LendRecord::getId,lendRecordID);
        LendRecord lendRecord = lendRecordMapper.selectOne(lendRecordLambdaQueryWrapper);
        lendRecord.setStatus("1");
        lendRecord.setReturnTime(new Date());
        lendRecordMapper.updateById(lendRecord);
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
        specificbookMapper.updateById(specificbook);
        return Result.success();
    }



}
