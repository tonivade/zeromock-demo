/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

  List<Book> findAll();

  Optional<Book> findById(Integer id);

  Book save(Book book);

  Book update(Integer id, String title);

  void delete(Integer id);
}
