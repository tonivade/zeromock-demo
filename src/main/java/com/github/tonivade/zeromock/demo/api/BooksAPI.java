/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.api;

import com.github.tonivade.purefun.Consumer1;
import com.github.tonivade.purefun.Function1;
import com.github.tonivade.purefun.Function2;
import com.github.tonivade.purefun.Producer;
import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.zeromock.api.Bytes;
import com.github.tonivade.zeromock.api.Deserializers;
import com.github.tonivade.zeromock.api.HttpRequest;
import com.github.tonivade.zeromock.api.RequestHandler;
import com.github.tonivade.zeromock.api.Responses;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

import static com.github.tonivade.zeromock.api.Extractors.asInteger;
import static com.github.tonivade.zeromock.api.Extractors.body;
import static com.github.tonivade.zeromock.api.Extractors.pathParam;
import static com.github.tonivade.zeromock.api.Headers.contentJson;
import static com.github.tonivade.zeromock.api.Serializers.objectToJson;
import static java.util.Objects.requireNonNull;

public class BooksAPI {

  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = requireNonNull(service);
  }

  public RequestHandler findAll() {
    return Producer.of(service::findAll).asFunction()
        .andThen(ImmutableList::toList)
        .andThen(objectToJson())
        .andThen(Responses::ok)
        .andThen(contentJson())::apply;
  }

  public RequestHandler update() {
    return Function2.of(service::update).compose(getBookId(), getBookTitle())
        .liftTry()
        .andThen(x -> x.map(objectToJson()))
        .andThen(x -> x.map(Responses::ok))
        .andThen(x -> x.getOrElse(Responses::error))
        .andThen(contentJson())::apply;
  }

  public RequestHandler find() {
    return Function1.of(service::find).compose(getBookId())
        .andThen(x -> x.map(objectToJson()))
        .andThen(x -> x.map(Responses::ok))
        .andThen(x -> x.getOrElse(Responses::noContent))
        .andThen(contentJson())::apply;
  }

  public RequestHandler create() {
    return Function1.of(service::create).compose(getBookTitle())
        .liftTry()
        .andThen(x -> x.map(objectToJson()))
        .andThen(x -> x.map(Responses::created))
        .andThen(x -> x.getOrElse(Responses::error))
        .andThen(contentJson())::apply;
  }

  public RequestHandler delete() {
    return getBookId()
        .andThen(Consumer1.of(service::delete).asFunction())
        .liftTry()
        .andThen(x -> x.fold(Responses::error, Function1.cons(Responses.ok())))
        .andThen(contentJson())::apply;
  }

  private static Function1<HttpRequest, Integer> getBookId() {
    return pathParam(1).andThen(asInteger());
  }

  private static Function1<HttpRequest, String> getBookTitle() {
    return body().andThen(asBook()).andThen(Book::title);
  }

  private static Function1<Bytes, Book> asBook() {
    return Deserializers.jsonToObject(Book.class);
  }
}
