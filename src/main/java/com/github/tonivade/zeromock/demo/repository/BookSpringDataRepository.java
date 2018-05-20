/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BookRepository;

@Repository
public class BookSpringDataRepository implements BookRepository {
  @Autowired
  private BookDAO dao;

  @Override
  public List<Book> findAll() {
    return stream(dao.findAll().spliterator(), false).map(this::convert).collect(toList());
  }

  @Override
  public Optional<Book> findById(Integer id) {
    return dao.findById(id).map(this::convert);
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
  public Void delete(Integer id) {
    dao.deleteById(id);
    return null;
  }
  
  private BookEntity convert(Book book) {
    return new BookEntity(book.id(), book.title());
  }
  
  private Book convert(BookEntity entity) {
    return new Book(entity.getId(), entity.getTitle());
  }
}
