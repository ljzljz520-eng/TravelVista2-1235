package com.travelvista;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.travelvista.mapper")
public class TravelVistaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelVistaApplication.class, args);
    }

}
