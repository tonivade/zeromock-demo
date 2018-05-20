/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import com.github.tonivade.zeromock.core.Handler2;
import com.github.tonivade.zeromock.core.OptionalHandler;
import com.github.tonivade.zeromock.core.StreamHandler;
import com.github.tonivade.zeromock.core.Try;
import com.github.tonivade.zeromock.core.TryHandler;

public class BooksService {
  
  private final BookRepository repository;
  
  public BooksService(BookRepository repository) {
    this.repository = repository;
  }

  public <T> StreamHandler<T, Book> findAll() {
    return ignore -> repository.findAll().stream();
  }

  public OptionalHandler<Integer, Book> find() {
    return id -> repository.findById(id);
  }

  public TryHandler<String, Book> create() {
    return title -> Try.of(() -> repository.save(new Book(null, title)));
  }

  public Handler2<Integer, String, Try<Book>> update() {
    return (id, title) -> Try.of(() -> repository.save(new Book(id, title)));
  }

  public TryHandler<Integer, Void> delete() {
    return id -> Try.<Void>of(() -> repository.delete(id));
  }
}
