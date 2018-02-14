package com.github.tonivade.zeromock.demo.repository;

import java.util.LinkedList;
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
    List<Book> result = new LinkedList<>();
    for (BookEntity bookEntity : dao.findAll()) {
      result.add(convert(bookEntity));
    }
    return result;
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
