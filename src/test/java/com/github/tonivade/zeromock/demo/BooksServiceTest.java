/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo;

import static com.github.tonivade.zeromock.api.Bytes.asString;
import static com.github.tonivade.zeromock.api.Requests.delete;
import static com.github.tonivade.zeromock.api.Requests.get;
import static com.github.tonivade.zeromock.api.Requests.post;
import static com.github.tonivade.zeromock.api.Requests.put;
import static com.github.tonivade.zeromock.server.HttpClient.connectTo;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tonivade.zeromock.api.Bytes;
import com.github.tonivade.zeromock.api.Deserializers;
import com.github.tonivade.zeromock.api.HttpResponse;
import com.github.tonivade.zeromock.api.HttpStatus;
import com.github.tonivade.zeromock.api.Serializers;
import com.github.tonivade.zeromock.demo.domain.Book;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksServiceTest {
  
  private static final String STORE_URL = "http://localhost:8080/store";
  private static final String INSERT_INTO_BOOK = "insert into book (id, title) values (1, 'title')";
  private static final String DELETE_FROM_BOOK = "delete from book";

  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void findsBooks() {
    HttpResponse response = connectTo(STORE_URL).request(get("/books"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals(asList(new Book(1, "title")), asBooks(response.body()));
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK})
  public void findsBookNotFound() {
    HttpResponse response = connectTo(STORE_URL).request(get("/books/1"));
    
    assertEquals(HttpStatus.NO_CONTENT, response.status());
    assertEquals("", asString(response.body()));
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void findsBook() {
    HttpResponse response = connectTo(STORE_URL).request(get("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals(new Book(1, "title"), asBook(response.body()));
  }
  
  @Test
  @Sql(statements = DELETE_FROM_BOOK)
  public void createsBook() {
    HttpResponse response = connectTo(STORE_URL)
        .request(post("/books").withBody(asJson(new Book(null, "create"))));
    
    assertEquals(HttpStatus.CREATED, response.status());
    assertEquals(new Book(1, "create"), asBook(response.body()));
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void deletesBook() {
    HttpResponse response = connectTo(STORE_URL).request(delete("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("", asString(response.body()));
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void updatesBook() {
    HttpResponse response = connectTo(STORE_URL)
        .request(put("/books/1").withBody(asJson(new Book(null, "update"))));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals(new Book(1, "update"), asBook(response.body()));
  }

  private Bytes asJson(Book book) {
    return Serializers.json().apply(book);
  }

  private Book asBook(Bytes body) {
    return Deserializers.json(Book.class).apply(body);
  }

  private List<Book> asBooks(Bytes body) {
    return Deserializers.<List<Book>>json(listOfBooks()).apply(body);
  }
  
  private Type listOfBooks() {
    return new TypeToken<List<Book>>(){}.getType();
  }
}
