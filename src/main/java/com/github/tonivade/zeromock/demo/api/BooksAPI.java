/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import static com.github.tonivade.zeromock.api.Extractors.asInteger;
import static com.github.tonivade.zeromock.api.Extractors.body;
import static com.github.tonivade.zeromock.api.Extractors.pathParam;
import static com.github.tonivade.zeromock.api.Headers.contentJson;
import static com.github.tonivade.zeromock.api.Serializers.empty;
import static com.github.tonivade.zeromock.api.Serializers.json;
import static com.github.tonivade.zeromock.core.OptionalHandler.adapt;
import static com.github.tonivade.zeromock.core.TryHandler.adapt;
import static java.util.stream.Collectors.toList;

import com.github.tonivade.zeromock.api.Bytes;
import com.github.tonivade.zeromock.api.Deserializers;
import com.github.tonivade.zeromock.api.HttpRequest;
import com.github.tonivade.zeromock.api.RequestHandler;
import com.github.tonivade.zeromock.api.Responses;
import com.github.tonivade.zeromock.core.Handler1;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

public class BooksAPI {
  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = service;
  }

  public RequestHandler findAll() {
    return service.findAll()
        .collect(toList())
        .andThen(json())
        .andThen(Responses::ok)
        .andThen(contentJson())::handle;
  }

  public RequestHandler update() {
    return adapt(service.update().compose(getBookId(), getBookTitle()))
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::error)
        .andThen(contentJson())::handle;
  }

  public RequestHandler find() {
    return adapt(getBookId().andThen(service.find()))
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::noContent)
        .andThen(contentJson())::handle;
  }

  public RequestHandler create() {
    return adapt(getBookTitle().andThen(service.create()))
        .map(json())
        .map(Responses::created)
        .orElse(Responses::error)
        .andThen(contentJson())::handle;
  }

  public RequestHandler delete() {
    return adapt(getBookId().andThen(service.delete()))
        .map(empty())
        .map(Responses::ok)
        .orElse(Responses::error)
        .andThen(contentJson())::handle;
  }

  private static Handler1<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Handler1<HttpRequest, String> getBookTitle() {
    return body().andThen(asBook()).andThen(Book::title);
  }

  private static Handler1<Bytes, Book> asBook() {
    return Deserializers.<Book>json(Book.class);
  }
}
