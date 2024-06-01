package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 *
 * @TableName book
 */
@TableName(value ="book")
public class Book implements Serializable {
    /**
     * 图书编号
     */
    @TableId
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 图书编号
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * 图书编号
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 出版社
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 出版社
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * 剩余数量
     */
    public Long getLeftNumber() {
        return leftNumber;
    }

    /**
     * 剩余数量
     */
    public void setLeftNumber(Long leftNumber) {
        this.leftNumber = leftNumber;
    }

    /**
     * 总共数量
     */
    public Long getTotalNumber() {
        return totalNumber;
    }

    /**
     * 总共数量
     */
    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    /**
     * 总借次数
     */
    public Integer getBorrownum() {
        return borrownum;
    }

    /**
     * 总借次数
     */
    public void setBorrownum(Integer borrownum) {
        this.borrownum = borrownum;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Book other = (Book) that;
        return (this.getIsbn() == null ? other.getIsbn() == null : this.getIsbn().equals(other.getIsbn()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getAuthor() == null ? other.getAuthor() == null : this.getAuthor().equals(other.getAuthor()))
                && (this.getPublisher() == null ? other.getPublisher() == null : this.getPublisher().equals(other.getPublisher()))
                && (this.getLeftNumber() == null ? other.getLeftNumber() == null : this.getLeftNumber().equals(other.getLeftNumber()))
                && (this.getTotalNumber() == null ? other.getTotalNumber() == null : this.getTotalNumber().equals(other.getTotalNumber()))
                && (this.getBorrownum() == null ? other.getBorrownum() == null : this.getBorrownum().equals(other.getBorrownum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIsbn() == null) ? 0 : getIsbn().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getAuthor() == null) ? 0 : getAuthor().hashCode());
        result = prime * result + ((getPublisher() == null) ? 0 : getPublisher().hashCode());
        result = prime * result + ((getLeftNumber() == null) ? 0 : getLeftNumber().hashCode());
        result = prime * result + ((getTotalNumber() == null) ? 0 : getTotalNumber().hashCode());
        result = prime * result + ((getBorrownum() == null) ? 0 : getBorrownum().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", isbn=").append(isbn);
        sb.append(", name=").append(name);
        sb.append(", author=").append(author);
        sb.append(", publisher=").append(publisher);
        sb.append(", leftNumber=").append(leftNumber);
        sb.append(", totalNumber=").append(totalNumber);
        sb.append(", borrownum=").append(borrownum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}