package com.n3o.springwebfluxrest.repositories;

import com.n3o.springwebfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
