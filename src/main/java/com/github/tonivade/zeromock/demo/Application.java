/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo;

import static com.github.tonivade.zeromock.MockHttpServer.listenAt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.tonivade.zeromock.HttpService;
import com.github.tonivade.zeromock.MockHttpServer;

@SpringBootApplication
public class Application {
  
  @Bean(initMethod = "start", destroyMethod = "stop")
  public MockHttpServer server(HttpService books) {
    return listenAt(8080).mount("/store", books);
  }
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
