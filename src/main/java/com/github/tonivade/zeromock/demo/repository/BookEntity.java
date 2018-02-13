package com.github.tonivade.zeromock.demo.repository;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class BookEntity {
  @Column
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @Column
  private String title;

  public BookEntity() { }

  public BookEntity(Integer id, String title) {
    this.id = id;
    setTitle(title);
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  protected void setId(Integer id) {
    this.id = requireNonNull(id);
  }

  protected void setTitle(String title) {
    this.title = requireNonNull(title);
  }
}
