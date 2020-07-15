package com.n3o.springwebfluxrest.controllers;

import com.n3o.springwebfluxrest.domain.Category;
import com.n3o.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.n3o.springwebfluxrest.domain.Category.builder;

class CategoryControllerTest {
    WebTestClient webTestClient;
    CategoryController categoryController;
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(builder().description("cat1").build(),
                                      builder().description("bla bla").build()));
        webTestClient.get().uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(categoryRepository.findById("someId"))
                .willReturn(Mono.just(builder().description("cat1").build()));

        webTestClient.get().uri("/api/v1/category/someId")
                .exchange()
                .expectBody(Category.class);
    }
}