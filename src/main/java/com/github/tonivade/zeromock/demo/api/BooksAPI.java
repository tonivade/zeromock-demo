/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import static com.github.tonivade.zeromock.core.Combinators.adapt;
import static com.github.tonivade.zeromock.core.Combinators.join;
import static com.github.tonivade.zeromock.core.Combinators.map;
import static com.github.tonivade.zeromock.core.Combinators.orElse;
import static com.github.tonivade.zeromock.core.Combinators.split;
import static com.github.tonivade.zeromock.core.Extractors.asInteger;
import static com.github.tonivade.zeromock.core.Extractors.body;
import static com.github.tonivade.zeromock.core.Extractors.pathParam;
import static com.github.tonivade.zeromock.core.Handlers.created;
import static com.github.tonivade.zeromock.core.Handlers.ok;
import static com.github.tonivade.zeromock.core.Headers.contentJson;
import static com.github.tonivade.zeromock.core.Serializers.json;

import java.util.Optional;
import java.util.function.Function;

import com.github.tonivade.zeromock.core.Bytes;
import com.github.tonivade.zeromock.core.Deserializers;
import com.github.tonivade.zeromock.core.HttpRequest;
import com.github.tonivade.zeromock.core.HttpResponse;
import com.github.tonivade.zeromock.core.Responses;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

public class BooksAPI {
  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = service;
  }

  public Function<HttpRequest, HttpResponse> findAll() {
    return okJson(adapt(service::findAll));
  }

  public Function<HttpRequest, HttpResponse> update() {
    return okJson(join(getBookId(), getBookTitle()).andThen(split(service::update)));
  }

  public Function<HttpRequest, HttpResponse> find() {
    return okOrNoContentJson(getBookId().andThen(service::find));
  }

  public Function<HttpRequest, HttpResponse> create() {
    return createdJson(getBookTitle().andThen(service::create));
  }

  public Function<HttpRequest, HttpResponse> delete() {
    return getBookId().andThen(adapt(service::delete)).andThen(ok());
  }

  private static Function<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Function<HttpRequest, String> getBookTitle() {
    return body().andThen(asBook()).andThen(Book::title);
  }
  
  private static <T> Function<HttpRequest, HttpResponse> okJson(Function<HttpRequest, T> handler) {
    return ok(handler.andThen(json())).andThen(contentJson());
  }

  private static <T> Function<HttpRequest, HttpResponse> okOrNoContentJson(Function<HttpRequest, Optional<T>> handler) {
    return handler.andThen(map(json()))
        .andThen(map(Responses::ok))
        .andThen(orElse(Responses::noContent))
        .andThen(contentJson());
  }

  private static <T> Function<HttpRequest, HttpResponse> createdJson(Function<HttpRequest, T> handler) {
    return created(handler.andThen(json())).andThen(contentJson());
  }

  private static Function<Bytes, Book> asBook() {
    return Deserializers.<Book>json(Book.class);
  }
}
