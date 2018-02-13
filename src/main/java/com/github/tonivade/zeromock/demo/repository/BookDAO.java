package com.github.tonivade.zeromock.demo.repository;

import org.springframework.data.repository.CrudRepository;

public interface BookDAO extends CrudRepository<BookEntity, Integer> {

}
