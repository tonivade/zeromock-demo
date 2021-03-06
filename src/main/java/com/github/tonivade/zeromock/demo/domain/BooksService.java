/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.purefun.type.Option;

import static java.util.Objects.requireNonNull;

public class BooksService {
  
  private final BookRepository repository;
  
  public BooksService(BookRepository repository) {
    this.repository = requireNonNull(repository);
  }

  public ImmutableList<Book> findAll() {
    return repository.findAll();
  }

  public Option<Book> find(Integer id) {
    return repository.findById(id);
  }

  public  Book create(String title) {
    return repository.save(new Book(null, title));
  }

  public Book update(Integer id, String title) {
    return repository.save(new Book(id, title));
  }

  public void delete(Integer id) {
    repository.delete(id);
  }
}
