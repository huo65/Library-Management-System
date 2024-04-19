package com.example.demo;

import com.example.demo.utils.MailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
public class SpringBootApplicationTest {


    @Test
    public void test2() throws MessagingException {
        System.out.println("hi");
        MailUtils.sendMail("1599272181@qq.com", String.valueOf(6666));
        System.out.println("发送完毕");
    }
}
