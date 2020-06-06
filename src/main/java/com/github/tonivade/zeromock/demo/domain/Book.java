/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.domain;

import com.github.tonivade.purefun.Equal;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class Book {

  private static final Equal<Book> EQUAL = Equal.<Book>of().comparing(x -> x.id).comparing(x -> x.title);

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
    return EQUAL.applyTo(this, obj);
  }
}
