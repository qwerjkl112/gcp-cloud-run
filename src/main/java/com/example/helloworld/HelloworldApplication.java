
package com.example.helloworld;

import com.example.helloworld.dao.UserDistanceDao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HelloworldApplication {

  @Value("${NAME:World}")
  String name;

  @RestController
  class HelloworldController {
    @GetMapping("/")
    String hello() {
      return "Hello & GoodBye " + name + "!";
    }
  }

  @Bean
  UserDistanceDao getUserDistanceDao() {
      return new UserDistanceDao();
  }
  public static void main(String[] args) {
    SpringApplication.run(HelloworldApplication.class, args);
  }
}
