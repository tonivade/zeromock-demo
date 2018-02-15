/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import static com.github.tonivade.zeromock.Handlers.created;
import static com.github.tonivade.zeromock.Handlers.ok;
import static com.github.tonivade.zeromock.Handlers.okOrNoContent;
import static com.github.tonivade.zeromock.Predicates.delete;
import static com.github.tonivade.zeromock.Predicates.get;
import static com.github.tonivade.zeromock.Predicates.post;
import static com.github.tonivade.zeromock.Predicates.put;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.zeromock.HttpService;
import com.github.tonivade.zeromock.demo.domain.BookRepository;
import com.github.tonivade.zeromock.demo.domain.BooksService;

@Configuration
public class BooksConfig {
  @Bean
  public BooksService booksService(BookRepository repository) {
    return new BooksService(repository);
  }
  
  @Bean
  public BooksAPI booksApi(BooksService service) {
    return new BooksAPI(service);
  }
  
  @Bean
  public HttpService books(BooksAPI books) {
    return new HttpService("books")
      .when(post("/books"), created(books.create()))
      .when(get("/books"), ok(books.findAll()))
      .when(get("/books/:id"), okOrNoContent(books.find()))
      .when(delete("/books/:id"), ok(books.delete()))
      .when(put("/books/:id"), ok(books.update()));
  }
}
