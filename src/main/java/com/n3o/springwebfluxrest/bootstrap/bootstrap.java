package com.n3o.springwebfluxrest.bootstrap;

import com.n3o.springwebfluxrest.domain.Category;
import com.n3o.springwebfluxrest.domain.Vendor;
import com.n3o.springwebfluxrest.repositories.CategoryRepository;
import com.n3o.springwebfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public bootstrap(CategoryRepository categoryRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadVendors();
    }

    private void loadCategories() {
        if (categoryRepository.count().block() == 0){
            //loadData
            System.out.println("Loading Category Data");
            categoryRepository.save(Category.builder()
                    .description("Fruits").build()).block();
            categoryRepository.save(Category.builder()
                    .description("Nuts").build()).block();
            categoryRepository.save(Category.builder()
                    .description("Bread").build()).block();
            categoryRepository.save(Category.builder()
                    .description("Milk").build()).block();
            categoryRepository.save(Category.builder()
                    .description("Butter").build()).block();
            System.out.println("Loaded Categories: "+ categoryRepository.count().block());
        }
    }

    private void loadVendors() {
        System.out.println("Loading Vendor Data");
        vendorRepository.save(Vendor.builder()
                .firstName("N3O").lastName("Choudhary").build()).block();
        vendorRepository.save(Vendor.builder()
                .firstName("Nil").lastName("Choudhary").build()).block();
        vendorRepository.save(Vendor.builder()
                .firstName("Nilesh").lastName("Choudhary").build()).block();
        vendorRepository.save(Vendor.builder()
                .firstName("Neo").lastName("Choudhary").build()).block();
        System.out.println("Loaded vendors: "+ vendorRepository.count().block());
    }
}
