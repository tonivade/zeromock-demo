/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tonivade.zeromock.HttpClient;
import com.github.tonivade.zeromock.HttpResponse;
import com.github.tonivade.zeromock.HttpStatus;
import com.github.tonivade.zeromock.Requests;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksServiceTest {
  
  @Test
  public void findsBooks() {
    HttpClient client = new HttpClient("http://localhost:8080/store");
    
    HttpResponse response = client.request(Requests.get("/books"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("[Book(id:1,title:title)]", response.body());
  }
  
  @Test
  public void findsBook() {
    HttpClient client = new HttpClient("http://localhost:8080/store");
    
    HttpResponse response = client.request(Requests.get("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("Book(id:1,title:title)", response.body());
  }
  
  @Test
  public void createsBook() {
    HttpClient client = new HttpClient("http://localhost:8080/store");
    
    HttpResponse response = client.request(Requests.post("/books").withBody("create"));
    
    assertEquals(HttpStatus.CREATED, response.status());
    assertEquals("Book(id:1,title:create)", response.body());
  }
  
  @Test
  public void deletesBook() {
    HttpClient client = new HttpClient("http://localhost:8080/store");
    
    HttpResponse response = client.request(Requests.delete("/books/1"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals(null, response.body());
  }
  
  @Test
  public void updatesBook() {
    HttpClient client = new HttpClient("http://localhost:8080/store");
    
    HttpResponse response = client.request(Requests.put("/books/1").withBody("update"));
    
    assertEquals(HttpStatus.OK, response.status());
    assertEquals("Book(id:1,title:update)", response.body());
  }
}
