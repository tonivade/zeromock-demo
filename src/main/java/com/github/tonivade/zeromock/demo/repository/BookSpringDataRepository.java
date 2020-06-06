/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.repository;

import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.purefun.type.Option;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.github.tonivade.zeromock.demo.domain.BookRepository;
import org.springframework.stereotype.Repository;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

@Repository
public class BookSpringDataRepository implements BookRepository {

  private final BookDAO dao;

  public BookSpringDataRepository(BookDAO dao) {
    this.dao = requireNonNull(dao);
  }

  @Override
  public ImmutableList<Book> findAll() {
    return ImmutableList.from(stream(dao.findAll().spliterator(), false).map(this::convert));
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
