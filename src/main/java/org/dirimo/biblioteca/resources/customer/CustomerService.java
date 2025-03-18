package org.dirimo.biblioteca.resources.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer customer) {
        customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scaffale con id: " + id + " non trovato."));
        return customerRepository.save(customer);
    }

    public List<Customer> createBulk(List<Customer> customers) {
        return customerRepository.saveAll(customers);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
