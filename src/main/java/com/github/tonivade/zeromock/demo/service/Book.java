package com.github.tonivade.zeromock.demo.service;

public class Book {
  private final Integer id;
  private final String title;

  public Book(Integer id, String title) {
    this.id = id;
    this.title = title;
  }
  
  @Override
  public String toString() {
    return "Book(id:" + id + ",title:" + title + ")";
  }
}
