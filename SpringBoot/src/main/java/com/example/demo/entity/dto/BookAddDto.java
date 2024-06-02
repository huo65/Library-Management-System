package com.example.demo.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fyftless
 */
@Data
public class BookAddDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * isbn（二维码）
     */
    private String isbn;

    /**
     * 名称（二维码）
     */
    private String name;

    /**
     * 作者 （二维码）
     */
    private String author;

    /**
     * 出版社（二维码）
     */
    private String publisher;

    /**
     * 存放位置(手动输入）
     */
    private String location;
}
