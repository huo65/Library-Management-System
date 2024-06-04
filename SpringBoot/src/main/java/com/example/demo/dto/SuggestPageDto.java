package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fyftless
 */
@Data
public class SuggestPageDto implements Serializable{

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页容量
     */
    private Integer pageSize;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 回复者名称
     */
    private String replyName;

    /**
     * 是否回复
     */
    private Integer replied;
}
