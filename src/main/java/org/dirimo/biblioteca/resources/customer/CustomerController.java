package org.dirimo.biblioteca.resources.customer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("Customer")
public class CustomerController {
    private final CustomerService customerService;

    // Get all customers
    @GetMapping("/")
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public Customer getyId(@PathVariable Long id) {
        return customerService.getById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id " + id + " non trovato."));
    }

    // Add a new customer
    @PostMapping("/")
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    // Update a customer
    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}
