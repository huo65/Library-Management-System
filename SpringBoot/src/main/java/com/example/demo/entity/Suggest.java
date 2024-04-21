package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fyftless
 */
@TableName("suggest")
@Data
public class Suggest {

    private Integer id;
    private String nickName;
    private String suggestContent;
    private String replyContent;
    private Integer replied;
    private String replyName;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
