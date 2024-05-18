package com.example.demo.service;

import com.example.demo.entity.Fine;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author huozj
* @description 针对表【Fine】的数据库操作Service
* @createDate 2024-05-18 22:00:02
*/
public interface FineService extends IService<Fine> {
     void batchInsertFine(List<Fine> fineList);
}
