/*
 * Copyright (c) 2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.zeromock.demo.repository;

import org.springframework.data.repository.CrudRepository;

public interface BookDAO extends CrudRepository<BookEntity, Integer> {

}
