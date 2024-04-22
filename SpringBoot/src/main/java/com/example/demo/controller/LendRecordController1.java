package com.example.demo.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.commom.Result;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.LendRecord;
import com.example.demo.mapper.LendRecordMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/LendRecord1")
public class LendRecordController1 {
    @Resource
    LendRecordMapper lendRecordMapper;
    @PutMapping
    public  Result<?> update2( @RequestBody LendRecord lendRecord){
        log.info("*****************************************"+lendRecord);
        UpdateWrapper<LendRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("isbn",lendRecord.getIsbn()).eq("reader_id",lendRecord.getReaderId()).eq("lend_time",lendRecord.getLendTime());
        LendRecord record = new LendRecord();
        record.setStatus(String.valueOf(lendRecord.getStatus()));
        record.setReturnTime(lendRecord.getLendTime());
        lendRecordMapper.update(record, updateWrapper);
        return Result.success();
    }



}
