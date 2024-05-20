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

//        1. 根据读者id删除之前记录
        if (fineList != null){
            Fine fine = fineList.get(0);
            LambdaQueryWrapper<Fine> query = Wrappers.lambdaQuery();
            query.eq(Fine::getStatus,0);
            query.eq(Fine::getReaderid,fine.getReaderid());
            List<Fine> list = fineMapper.selectList(query);
            System.out.println("更新记录："+list);
            this.removeBatchByIds(list);
        }
//        2. 重新插入
            this.saveBatch(fineList);

//        3. 修改对应记录
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




