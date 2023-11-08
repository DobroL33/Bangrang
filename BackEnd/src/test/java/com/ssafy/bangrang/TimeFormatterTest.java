package com.ssafy.bangrang;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Transactional
public class TimeFormatterTest {

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @Test
    void test(){
        System.out.println(LocalDateTime.now().format(dateTimeFormatter));
    }
}
