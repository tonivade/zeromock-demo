/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import java.util.List;
import java.util.Optional;

public class BooksService {
  
  private final BookRepository repository;
  
  public BooksService(BookRepository repository) {
    this.repository = repository;
  }

  public List<Book> findAll() {
    return repository.findAll();
  }

  public Optional<Book> find(Integer id) {
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
