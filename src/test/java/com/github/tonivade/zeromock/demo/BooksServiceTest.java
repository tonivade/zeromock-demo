/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo;

import static com.github.tonivade.zeromock.Requests.delete;
import static com.github.tonivade.zeromock.Requests.get;
import static com.github.tonivade.zeromock.Requests.post;
import static com.github.tonivade.zeromock.Requests.put;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tonivade.zeromock.HttpClient;
import com.github.tonivade.zeromock.HttpResponse;
import com.github.tonivade.zeromock.HttpStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksServiceTest {
  
  private static final String STORE_URL = "http://localhost:8080/store";
  private static final String INSERT_INTO_BOOK = "insert into book (id, title) values (1, 'title')";
  private static final String DELETE_FROM_BOOK = "delete from book";

  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void findsBooks() {
    HttpClient client = new HttpClient(STORE_URL);
    
    HttpResponse response = client.request(get("/books"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("[Book(id:1,title:title)]", response.body());
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void findsBook() {
    HttpClient client = new HttpClient(STORE_URL);
    
    HttpResponse response = client.request(get("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("Book(id:1,title:title)", response.body());
  }
  
  @Test
  @Sql(statements = DELETE_FROM_BOOK)
  public void createsBook() {
    HttpClient client = new HttpClient(STORE_URL);
    
    HttpResponse response = client.request(post("/books").withBody("create"));
    
    assertEquals(HttpStatus.CREATED, response.status());
    assertEquals("Book(id:1,title:create)", response.body());
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void deletesBook() {
    HttpClient client = new HttpClient(STORE_URL);
    
    HttpResponse response = client.request(delete("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals(null, response.body());
  }
  
  @Test
  @Sql(statements = { DELETE_FROM_BOOK, INSERT_INTO_BOOK})
  public void updatesBook() {
    HttpClient client = new HttpClient(STORE_URL);
    
    HttpResponse response = client.request(put("/books/1").withBody("update"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("Book(id:1,title:update)", response.body());
  }
}
