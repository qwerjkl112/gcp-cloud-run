
package com.example.helloworld;

import com.example.helloworld.dao.UserDistanceDao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloworldApplication {

  @Bean
  UserDistanceDao getUserDistanceDao() {
      return new UserDistanceDao();
  }
  public static void main(String[] args) {
    SpringApplication.run(HelloworldApplication.class, args);
  }
}
