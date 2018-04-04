/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import static com.github.tonivade.zeromock.core.Extractors.asInteger;
import static com.github.tonivade.zeromock.core.Extractors.body;
import static com.github.tonivade.zeromock.core.Extractors.pathParam;
import static com.github.tonivade.zeromock.core.Handler1.adapt;
import static com.github.tonivade.zeromock.core.Handler2.adapt;
import static com.github.tonivade.zeromock.core.Handlers.created;
import static com.github.tonivade.zeromock.core.Handlers.ok;
import static com.github.tonivade.zeromock.core.Headers.contentJson;
import static com.github.tonivade.zeromock.core.Serializers.json;

import com.github.tonivade.zeromock.core.Bytes;
import com.github.tonivade.zeromock.core.Deserializers;
import com.github.tonivade.zeromock.core.Handler1;
import com.github.tonivade.zeromock.core.HttpRequest;
import com.github.tonivade.zeromock.core.OptionalHandler;
import com.github.tonivade.zeromock.core.RequestHandler;
import com.github.tonivade.zeromock.core.Responses;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

public class BooksAPI {
  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = service;
  }

  public RequestHandler findAll() {
    return okJson(adapt(service::findAll));
  }

  public RequestHandler update() {
    return okJson(adapt(service::update).compose(getBookId(), getBookTitle()));
  }

  public RequestHandler find() {
    return okOrNoContentJson(getBookId().andThen(service::find)::handle);
  }

  public RequestHandler create() {
    return createdJson(getBookTitle().andThen(service::create));
  }

  public RequestHandler delete() {
    return getBookId().andThen(adapt(service::delete)).andThen(ok())::handle;
  }

  private static Handler1<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Handler1<HttpRequest, String> getBookTitle() {
    return body().andThen(asBook()).andThen(Book::title);
  }
  
  private static <T> RequestHandler okJson(Handler1<HttpRequest, T> handler) {
    return ok(handler.andThen(json())).postHandle(contentJson());
  }

  private static <T> RequestHandler okOrNoContentJson(OptionalHandler<HttpRequest, T> handler) {
    return handler.map(json())
        .map(Responses::ok)
        .orElse(Responses::noContent)
        .andThen(contentJson())::handle;
  }

  private static <T> RequestHandler createdJson(Handler1<HttpRequest, T> handler) {
    return created(handler.andThen(json())).postHandle(contentJson());
  }

  private static Handler1<Bytes, Book> asBook() {
    return Deserializers.<Book>json(Book.class);
  }
}
