package com.leyou.upload.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.internal.operators.OnSubscribeDelaySubscriptionOther;

@SpringBootTest
@RunWith(SpringRunner.class)
public class xuexiTest {

    //public static void main(String[] args) {
    //  test();
    //   }
    public void add(Byte b) {
        b++;
    }

    @Test
    public void test() {
        Byte a = 127;
        Byte b = 127;

        add(++a);
        System.out.println(a + "");
        add(b);
        System.out.println(b + "");
    }
}
