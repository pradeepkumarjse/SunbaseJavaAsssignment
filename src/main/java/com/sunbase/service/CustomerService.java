package com.sunbase.service;

import com.sunbase.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer updatedCustomer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    void deleteCustomer(Long id);
}
