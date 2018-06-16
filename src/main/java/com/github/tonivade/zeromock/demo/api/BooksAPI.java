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
import com.github.tonivade.zeromock.core.Option;
import com.github.tonivade.zeromock.core.Producer;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BooksService;

public class BooksAPI {
  private final BooksService service;
  
  public BooksAPI(BooksService service) {
    this.service = service;
  }

  public RequestHandler findAll() {
    Producer<ImmutableList<Book>> findAll = service::findAll;
    return findAll.asFunction()
        .andThen(ImmutableList::toList)
        .andThen(json())
        .andThen(Responses::ok)
        .andThen(contentJson())::apply;
  }

  public RequestHandler update() {
    Function2<Integer, String, Book> update = service::update;
    return update.compose(getBookId(), getBookTitle())
        .liftTry()
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::error)
        .andThen(contentJson())::apply;
  }

  public RequestHandler find() {
    Function1<Integer, Option<Book>> find = service::find;
    return find.compose(getBookId())
        .liftOption()
        .flatten()
        .map(json())
        .map(Responses::ok)
        .orElse(Responses::noContent)
        .andThen(contentJson())::apply;
  }

  public RequestHandler create() {
    Function1<String, Book> create = service::create;
    return create.compose(getBookTitle())
        .liftTry()
        .map(json())
        .map(Responses::created)
        .orElse(Responses::error)
        .andThen(contentJson())::apply;
  }

  public RequestHandler delete() {
    Consumer1<Integer> delete = service::delete;
    return getBookId()
        .andThen(delete.asFunction())
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
