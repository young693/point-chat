package com.point.chat.pointadmin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
public class PrintBeanName {
    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
    }

}



