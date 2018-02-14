/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "book")
@Entity
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private Integer id;
  @Column
  private String title;

  public BookEntity() { }

  public BookEntity(Integer id, String title) {
    this.id = id;
    this.title = title;
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  protected void setId(Integer id) {
    this.id = id;
  }

  protected void setTitle(String title) {
    this.title = title;
  }
}
