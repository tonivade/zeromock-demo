package com.github.tonivade.zeromock.demo.domain;

import static java.util.Objects.requireNonNull;

public class Book {
  private final Integer id;
  private final String title;

  public Book(Integer id, String title) {
    this.id = requireNonNull(id);
    this.title = requireNonNull(title);
  }
  
  @Override
  public String toString() {
    return "Book(id:" + id + ",title:" + title + ")";
  }
}
