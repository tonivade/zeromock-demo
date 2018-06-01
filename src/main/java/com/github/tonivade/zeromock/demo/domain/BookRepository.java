/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import com.github.tonivade.zeromock.core.InmutableList;
import com.github.tonivade.zeromock.core.Option;

public interface BookRepository {

  InmutableList<Book> findAll();

  Option<Book> findById(Integer id);

  Book save(Book book);

  Book update(Integer id, String title);

  void delete(Integer id);
}
