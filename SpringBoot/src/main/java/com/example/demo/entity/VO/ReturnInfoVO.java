package com.example.demo.entity.VO;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * ReturnInfoVO
 */
public class ReturnInfoVO {
//    Specific 信息
    /**
     * ID
     */
    private long bookId;
    /**
     * 图书编号
     */
    private String isbn;
    /**
     * 存放位置
     */
    private String location;
    /**
     * 名称
     */
    private String bookName;
//    借书信息
    /**
     * 读者
     */
    private Long readerId;

    private String readerName;

    /**
     * 借书时间
     */
    private Date lendTime;
    /**
     * 借书记录id（不需要展示）
     */
    private String recordId;
//    用户视图ID
//    private String bookWithUserId;
    /**
     * 0时返回
     */
//    private String status;

    public long getBookId() { return bookId; }
    public void setBookId(long value) { this.bookId = value; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String value) { this.isbn = value; }

    public Date getLendTime() { return lendTime; }
    public void setLendTime(Date value) { this.lendTime = value; }

    public String getLocation() { return location; }
    public void setLocation(String value) { this.location = value; }

    public String getBookName() { return bookName; }
    public void setBookName(String value) { this.bookName = value; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long value) { this.readerId = value; }

    public String getRecordId() { return recordId; }
    public void setRecordId(String value) { this.recordId = value; }
//
//    public String getBookWithUserId() { return bookWithUserId; }
//    public void setBookWithUserId(String value) { this.bookWithUserId = value; }
//
//
    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }
}
