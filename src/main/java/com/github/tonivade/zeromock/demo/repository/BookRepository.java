package com.github.tonivade.zeromock.demo.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tonivade.zeromock.demo.domain.Book;

@Component
public class BookRepository {
  @Autowired
  private BookDAO dao;

  public List<Book> findAll() {
    return stream(dao.findAll().spliterator(), false).map(this::convert).collect(toList());
  }

  public Optional<Book> findById(Integer id) {
    return dao.findById(id).map(this::convert);
  }

  public Book create(String title) {
    return convert(dao.save(new BookEntity(null, title)));
  }

  public Book update(Integer id, String title) {
    return convert(dao.save(new BookEntity(id, title)));
  }

  public void delete(Integer id) {
    dao.deleteById(id);
  }
  
  private Book convert(BookEntity entity) {
    return new Book(entity.getId(), entity.getTitle());
  }
}
