/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import static com.github.tonivade.zeromock.Extractors.asInteger;
import static com.github.tonivade.zeromock.Extractors.asString;
import static com.github.tonivade.zeromock.Extractors.body;
import static com.github.tonivade.zeromock.Extractors.pathParam;
import static com.github.tonivade.zeromock.Handlers.force;
import static com.github.tonivade.zeromock.Handlers.join;
import static com.github.tonivade.zeromock.Handlers.split;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tonivade.zeromock.HttpRequest;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

@Component
public class BooksAPI {
  @Autowired
  private BooksService service;

  public Supplier<Object> findAll() {
    return service::findAll;
  }

  public Function<HttpRequest, Book> update() {
    return join(getBookId(), getBookTitle()).andThen(split(service::update));
  }

  public Function<HttpRequest, Optional<Book>> find() {
    return getBookId().andThen(service::find);
  }

  public Function<HttpRequest, Book> create() {
    return body().andThen(asString()).andThen(service::create);
  }

  public Function<HttpRequest, Void> delete() {
    return getBookId().andThen(force(service::delete));
  }

  private static Function<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Function<HttpRequest, String> getBookTitle() {
    return body().andThen(asString());
  }
}
