/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class Book {
  private final Integer id;
  private final String title;

  public Book(Integer id, String title) {
    this.id = id;
    this.title = requireNonNull(title);
  }

  public Integer id() {
    return id;
  }

  public String title() {
    return title;
  }
  
  @Override
  public String toString() {
    return "Book(id:" + id + ",title:" + title + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Book other = (Book) obj;
    if (!Objects.equals(other.id, this.id)) {
      return false;
    }
    if (!Objects.equals(other.title, this.title)) {
      return false;
    }
    return true;
  }
}
