package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("book")
@Data
public class Book {

    @TableId (type = IdType.AUTO)
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private Integer leftNumber;
    private Integer totalNumber;
    private Integer borrownum;

}
