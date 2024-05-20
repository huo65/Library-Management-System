package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.Fine;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.FineMapper;
import com.example.demo.mapper.LendRecordMapper;
import com.example.demo.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
* @author huozj
* @description 针对表【Fine】的数据库操作Service实现
* @createDate 2024-05-18 22:00:02
*/
@Service
public class FineServiceImpl extends ServiceImpl<FineMapper, Fine>
    implements FineService {
    @Autowired
    FineMapper fineMapper;
    @Resource
    BookWithUserMapper bookWithUserMapper;

    @Resource
    BookMapper bookMapper;

    @Resource
    LendRecordMapper lendRecordMapper;
    public void batchInsertFine(List<Fine> fineList) {
        for (Fine fine : fineList){
            LambdaQueryWrapper<Fine> query = Wrappers.lambdaQuery();
            query.eq(Fine::getIsbn,fine.getIsbn());
            query.eq(Fine::getReaderid,fine.getReaderid());
//            query.eq(Fine::getNumber,fine.getNumber());
            Fine oldFine = fineMapper.selectOne(query);
            if (oldFine != null){
                fineMapper.updateById(fine);
            }else {
                fineMapper.insert(fine);
            }
        }

//        修改对应记录的deadtime
        for (Fine fine : fineList){
            LambdaQueryWrapper<BookWithUser> bUwrapper = Wrappers.lambdaQuery();
            bUwrapper.eq(BookWithUser::getId,fine.getBuID());
            BookWithUser bU = bookWithUserMapper.selectOne(bUwrapper);
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            Date tomorrowAsDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
            bU.setDeadtime(tomorrowAsDate);
            bookWithUserMapper.updateById(bU);
        }


    }

}




