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

import com.github.tonivade.zeromock.api.Bytes;
import com.github.tonivade.zeromock.api.Deserializers;
import com.github.tonivade.zeromock.api.HttpRequest;
import com.github.tonivade.zeromock.api.RequestHandler;
import com.github.tonivade.zeromock.api.Responses;
import com.github.tonivade.zeromock.core.Consumer1;
import com.github.tonivade.zeromock.core.Function1;
import com.github.tonivade.zeromock.core.Function2;
import com.github.tonivade.zeromock.core.ImmutableList;
import com.github.tonivade.zeromock.core.OptionHandler;
import com.github.tonivade.zeromock.core.Producer;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

public class BooksAPI {
  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = service;
  }

  public RequestHandler findAll() {
    return Producer.of(service::findAll).asFunction()
        .andThen(ImmutableList::toList)
        .andThen(json())
        .andThen(Responses::ok)
        .andThen(contentJson())::apply;
  }

  public RequestHandler update() {
    return Function2.of(service::update).compose(getBookId(), getBookTitle())
        .liftTry()
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::error)
        .andThen(contentJson())::apply;
  }

  public RequestHandler find() {
    return OptionHandler.of(service::find).compose(getBookId())
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::noContent)
        .andThen(contentJson())::apply;
  }

  public RequestHandler create() {
    return Function1.of(service::create).compose(getBookTitle())
        .liftTry()
        .map(json())
        .map(Responses::created)
        .orElse(Responses::error)
        .andThen(contentJson())::apply;
  }

  public RequestHandler delete() {
    return getBookId()
        .andThen(Consumer1.of(service::delete).asFunction())
        .liftTry()
        .map(empty())
        .map(Responses::ok)
        .orElse(Responses::error)
        .andThen(contentJson())::apply;
  }

  private static Function1<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Function1<HttpRequest, String> getBookTitle() {
    return body().andThen(asBook()).andThen(Book::title);
  }

  private static Function1<Bytes, Book> asBook() {
    return Deserializers.<Book>json(Book.class);
  }
}
