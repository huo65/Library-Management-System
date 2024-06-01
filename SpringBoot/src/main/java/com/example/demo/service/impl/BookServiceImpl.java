package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;

import org.springframework.stereotype.Service;

/**
* @author 86132
* @description 针对表【book】的数据库操作Service实现
* @createDate 2024-06-01 09:53:24
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




