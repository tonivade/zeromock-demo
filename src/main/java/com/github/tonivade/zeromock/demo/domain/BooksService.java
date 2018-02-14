/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tonivade.zeromock.demo.repository.BookRepository;

@Component
public class BooksService {
  @Autowired
  private BookRepository repository;

  public List<Book> findAll() {
    return repository.findAll();
  }

  public Optional<Book> find(Integer id) {
    return repository.findById(id);
  }

  public Book create(String title) {
    return repository.create(title);
  }

  public Book update(Integer id, String title) {
    return repository.update(id, title);
  }

  public void delete(Integer id) {
    repository.delete(id);
  }
}
