package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("specificBook")
@Data
public class specificBook {
    
    @TableId (type = IdType.AUTO)
    private Integer id;
    private String isbn;
    private String name;
    private String status;
    private String location;
}
