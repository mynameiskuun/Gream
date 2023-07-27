package com.project.gream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.NumberFormat;
import java.util.Locale;

@Slf4j
@SpringBootTest
class GreamApplicationTests {

//    @Test
//    void contextLoads() {
//    }

//    @Test
//    void start() {
//        log.info("Test Start!");
//    }

    @Test
    public void 숫자형식_테스트() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

        int number = 200;
        String formattedNumber = numberFormat.format(number);
        log.info(formattedNumber);
    }
}
