package com.example.demo.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.commom.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.RegexUtils;
import com.example.demo.utils.MailUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/forget")
public class ForgetController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/getcode")
    public Result<?> getcode(@RequestParam String username) throws MessagingException {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);
        if (user == null){
            return Result.error("-1","用户名不存在");
        }
        String phone = user.getPhone();
        //校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("-1","手机号错误或未绑定手机号");
        }

        String code = RandomUtil.randomNumbers(6);  //六位随机验证码

        stringRedisTemplate.opsForValue().set(phone,code,5L, TimeUnit.MINUTES);  //将验证码存入redis，5分钟有效
        MailUtils.sendMail(phone,code);
        System.out.println(code);
        return Result.success();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, user.getUsername());
        User user1 = userMapper.selectOne(wrapper);
        String phone = user1.getPhone();
        System.out.println(phone);
        String code = stringRedisTemplate.opsForValue().get(phone);  //从redis中取出验证码
        if (code==null){
            return Result.error("-1","请先获取验证码");
        }

        if (user.getCode().equals(code)){
            user.setId(user1.getId());
            userMapper.updateById(user);
            stringRedisTemplate.delete(phone);
            return Result.success();
        }
        return Result.error("-1","验证码错误");
    }


}
