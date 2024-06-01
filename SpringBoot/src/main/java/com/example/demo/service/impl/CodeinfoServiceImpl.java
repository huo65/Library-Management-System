package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Codeinfo;
import com.example.demo.service.CodeinfoService;
import com.example.demo.mapper.CodeinfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 86132
* @description 针对表【codeinfo(条形码信息表)】的数据库操作Service实现
* @createDate 2024-06-01 11:01:32
*/
@Service
public class CodeinfoServiceImpl extends ServiceImpl<CodeinfoMapper, Codeinfo>
    implements CodeinfoService{

}




