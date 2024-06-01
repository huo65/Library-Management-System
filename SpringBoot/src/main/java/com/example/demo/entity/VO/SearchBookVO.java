package com.example.demo.entity.VO;

import com.example.demo.entity.Specificbook;
import lombok.Data;

import java.util.List;

/**
 * 1. @ClassName SearchBookVO
 * 2. @Description 展示书籍
 * 3. @Author huo
 * 4. @Date 2024/6/1 10:16
 */
@Data
public class SearchBookVO {

    /**
     * 图书编号
     */
    private String isbn;

    /**
     * 名称
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 剩余数量
     */
    private Long leftNumber;

    /**
     * 总共数量
     */
    private Long totalNumber;

    /**
     * 总借次数
     */
    private Integer borrownum;

    /**
     * 具体书籍列表
     */
    private List<Specificbook> specificbooks;


}
