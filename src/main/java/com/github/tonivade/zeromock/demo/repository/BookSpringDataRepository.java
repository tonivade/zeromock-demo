/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.repository;

import static java.util.stream.StreamSupport.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.tonivade.zeromock.core.InmutableList;
import com.github.tonivade.zeromock.core.Option;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BookRepository;

@Repository
public class BookSpringDataRepository implements BookRepository {
  @Autowired
  private BookDAO dao;

  @Override
  public InmutableList<Book> findAll() {
    return InmutableList.from(stream(dao.findAll().spliterator(), false).map(this::convert));
  }

  @Override
  public Option<Book> findById(Integer id) {
    return Option.from(dao.findById(id).map(this::convert));
  }

  @Override
  public Book save(Book book) {
    return convert(dao.save(convert(book)));
  }

  @Override
  public Book update(Integer id, String title) {
    return convert(dao.save(new BookEntity(id, title)));
  }

  @Override
  public void delete(Integer id) {
    dao.deleteById(id);
  }
  
  private BookEntity convert(Book book) {
    return new BookEntity(book.id(), book.title());
  }
  
  private Book convert(BookEntity entity) {
    return new Book(entity.getId(), entity.getTitle());
  }
}
