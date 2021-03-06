package com.n3o.springwebfluxrest.controllers;

import com.n3o.springwebfluxrest.domain.Vendor;
import com.n3o.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.n3o.springwebfluxrest.domain.Vendor.builder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(builder().firstName("abc").lastName("wfbwy").build(),
                        builder().firstName("asd").lastName("defw").build()));
        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById("someId"))
                .willReturn(Mono.just(builder().firstName("cat1").lastName("bdvwbe").build()));

        webTestClient.get().uri("/api/v1/vendors/someId")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void testCreate() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(builder().firstName("some name").build()));

        Mono<Vendor> vendorMono = Mono.just(builder().firstName("some fname").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdate() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(builder().firstName("some name").build()));

        Mono<Vendor> vendorMono = Mono.just(builder().firstName("some fname").build());

        webTestClient.put()
                .uri("/api/v1/vendors/someId")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatch() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Vendor> vendorMono = Mono.just(builder().firstName("fname").lastName("lname").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void testPatchWithSingleChange() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(builder().firstName("some fname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Vendor> vendorMono = Mono.just(builder().firstName("fname").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void testPatchWithNoChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Vendor> vendorMono = Mono.just(builder().build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }
}