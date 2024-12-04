package org.uestc.weglas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * main
 */
@EnableTransactionManagement
@SpringBootApplication
public class AiManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiManageApplication.class, args);
    }
}
