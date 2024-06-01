package com.example.demo.entity.VO;

/**
 * ReturnInfoVO
 */
public class ReturnInfoVO {
    /**
     * ID
     */
    private long id;
    /**
     * 图书编号
     */
    private String isbn;
    /**
     * 借书时间
     */
    private String lendTime;
    /**
     * 存放位置
     */
    private String location;
    /**
     * 名称
     */
    private String name;
    /**
     * 读者
     */
    private Long readerId;
    /**
     * 借书记录id（不需要展示）
     */
    private String recordId;
    /**
     * 0时返回
     */
    private String status;

    public long getid() { return id; }
    public void setid(long value) { this.id = value; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String value) { this.isbn = value; }

    public String getLendTime() { return lendTime; }
    public void setLendTime(String value) { this.lendTime = value; }

    public String getLocation() { return location; }
    public void setLocation(String value) { this.location = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long value) { this.readerId = value; }

    public String getRecordId() { return recordId; }
    public void setRecordId(String value) { this.recordId = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}
