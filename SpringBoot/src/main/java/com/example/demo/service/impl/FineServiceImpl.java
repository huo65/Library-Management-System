package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Fine;
import com.example.demo.service.FineService;
import com.example.demo.mapper.FineMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author huozj
* @description 针对表【Fine】的数据库操作Service实现
* @createDate 2024-05-18 22:00:02
*/
@Service
public class FineServiceImpl extends ServiceImpl<FineMapper, Fine>
    implements FineService {

    public void batchInsertFine(List<Fine> fineList) {

//        1. 根据读者id删除之前记录
//        2. 重新插入
        System.out.println("!!!!!"+fineList);
            this.saveBatch(fineList);
    }

}




