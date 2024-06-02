package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.Specificbook;
import com.example.demo.entity.VO.SearchBookVO;
import com.example.demo.entity.dto.BookAddDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.SpecificbookMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookMapper bookMapper;

    @Resource
    BookWithUserMapper bookWithUserMapper;

    @Resource
    SpecificbookMapper specificbookMapper;

    /**
     * 添加书本
     * @param bookAddDto 书本信息
     * @return result
     */
    @PostMapping
    @Transactional
    public Result<?> save(@RequestBody BookAddDto bookAddDto){

        // 存放本书
        Specificbook specificbook = new Specificbook();
        BeanUtils.copyProperties(bookAddDto, specificbook);
        specificbook.setStatus("1");
        specificbookMapper.insert(specificbook);

        // 书本归类，数量+1
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,bookAddDto.getIsbn());
        Book selectOne = bookMapper.selectOne(wrapper);
        if (selectOne != null){
            // 同类书已存在
            selectOne.setTotalNumber(selectOne.getTotalNumber() + 1);
            selectOne.setLeftNumber(selectOne.getLeftNumber() + 1);
            bookMapper.update(selectOne, wrapper);
        }else{
            // 新类书
            Book book = new Book();
            BeanUtils.copyProperties(bookAddDto, book);
            book.setTotalNumber(1L);
            book.setLeftNumber(1L);
            book.setBorrownum(0);
            bookMapper.insert(book);
        }
        return Result.success();
    }

    /**
     * 修改书本信息(仅能修改存放位置,其他信息不可修改)
     * @param specificbook 书本信息
     * @return result
     */
    @PutMapping
    public  Result<?> update(@RequestBody Specificbook specificbook){
        // 仅能修改地址，所以只更新specific
        specificbookMapper.updateById(specificbook);
        return Result.success();
    }

    /**
     * 删除书本
     * @param id 书本id
     * @return result
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Result<?> delete(@PathVariable Long id){
        LambdaQueryWrapper<Specificbook> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Specificbook::getIsbn, id);
        Specificbook specificbook = specificbookMapper.selectOne(wrapper);
        if ("0".equals(specificbook.getStatus())){
            return Result.error("-1", "书本未归还，无法下架");
        }
        specificbookMapper.deleteById(id);
        LambdaQueryWrapper<Book> bookWrapper = Wrappers.lambdaQuery();
        bookWrapper.eq(Book::getIsbn, specificbook.getIsbn());
        Book book = bookMapper.selectOne(bookWrapper);
        book.setTotalNumber(book.getTotalNumber() - 1);
        book.setLeftNumber(book.getLeftNumber() - 1);
        if (book.getTotalNumber() == 0) {
            bookMapper.delete(bookWrapper);
        }else{
            bookMapper.update(book, bookWrapper);
        }
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String isbnKeyword,
            @RequestParam(defaultValue = "") String nameKeyword,
            @RequestParam(defaultValue = "") String authorKeyword) {

        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(isbnKeyword), Book::getIsbn, isbnKeyword)
                .like(StringUtils.isNotBlank(nameKeyword), Book::getName, nameKeyword)
                .like(StringUtils.isNotBlank(authorKeyword), Book::getAuthor, authorKeyword)
                .orderByDesc(Book::getBorrownum);

        Page<Book> bookPage = new Page<>(pageNum, pageSize);
        bookMapper.selectPage(bookPage, wrapper);

        Map<String, List<Specificbook>> isbnToSpecificBooks = getSpecificBooksByIsbns(bookPage.getRecords().stream()
                .map(Book::getIsbn)
                .collect(Collectors.toList()));

        List<SearchBookVO> voList = bookPage.getRecords().stream()
                .map(book -> convertToSearchBookVo(book, isbnToSpecificBooks.get(book.getIsbn())))
                .collect(Collectors.toList());

        Page<SearchBookVO> voPage = new Page<>(bookPage.getCurrent(), bookPage.getSize(), bookPage.getTotal());
        voPage.setRecords(voList);

        return Result.success(voPage);
    }

    private Map<String, List<Specificbook>> getSpecificBooksByIsbns(List<String> isbns) {
        if (CollectionUtils.isEmpty(isbns)) {
            return Collections.emptyMap();
        }

        List<Specificbook> specificbooks = specificbookMapper.selectList(
                Wrappers.<Specificbook>lambdaQuery().in(Specificbook::getIsbn, isbns));

        return specificbooks.stream()
                .collect(Collectors.groupingBy(Specificbook::getIsbn));
    }

    public SearchBookVO convertToSearchBookVo(Book book, List<Specificbook> specificbooks) {
        SearchBookVO vo = new SearchBookVO();
        vo.setIsbn(book.getIsbn());
        vo.setName(book.getName());
        vo.setAuthor(book.getAuthor());
        vo.setPublisher(book.getPublisher());
        vo.setLeftNumber(book.getLeftNumber());
        vo.setTotalNumber(book.getTotalNumber());
        vo.setBorrownum(book.getBorrownum());
        vo.setSpecificbooks(specificbooks == null ? Collections.emptyList() : specificbooks);
        return vo;
    }
}
