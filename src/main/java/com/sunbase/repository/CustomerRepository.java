package com.sunbase.repository;

import com.sunbase.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
        Optional<Customer> findByEmail(String email);

}
