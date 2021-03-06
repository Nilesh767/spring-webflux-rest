package com.n3o.springwebfluxrest.controllers;

import com.n3o.springwebfluxrest.domain.Category;
import com.n3o.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.n3o.springwebfluxrest.domain.Category.builder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

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
        given(categoryRepository.findAll())
                .willReturn(Flux.just(builder().description("cat1").build(),
                                      builder().description("bla bla").build()));
        webTestClient.get().uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById("someId"))
                .willReturn(Mono.just(builder().description("cat1").build()));

        webTestClient.get().uri("/api/v1/categories/someId")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void testCreateCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(builder().description("some description").build()));

        Mono<Category> categoryMono = Mono.just(builder().description("some data").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdate() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(builder().build()));
        Mono<Category> categoryMono = Mono.just(builder().description("bla bla").build());

        webTestClient.put()
                .uri("/api/v1/categories/value")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatch() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Category> categoryMono = Mono.just(builder().description("bla bla").build());

        webTestClient.patch()
                .uri("/api/v1/categories/value")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository).save(any());
    }

    @Test
    void testPatchWithNoChanges() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Category> categoryMono = Mono.just(builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/value")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository, never()).save(any());
    }
}