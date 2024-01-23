package com.sunbase.serviceimpl;

import com.sunbase.config.JwtTokenUtil;
import com.sunbase.entity.Customer;
import com.sunbase.exception.ResourceNotFoundException;
import com.sunbase.repository.CustomerRepository;
import com.sunbase.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${remoteCustomersApiUrl}")
    private String remoteCustomersApiUrl;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        updatedCustomer.setId(id);
        return customerRepository.save(updatedCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public void syncCustomers() {
        // Fetch customers from the remote API
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtTokenUtil.getBearerToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Customer[] remoteCustomers = restTemplate.exchange(remoteCustomersApiUrl, HttpMethod.GET, entity, Customer[].class).getBody();

        // Sync customers with the local database
        if (remoteCustomers != null) {
            for (Customer remoteCustomer : remoteCustomers) {
                Optional<Customer> existingCustomer = customerRepository.findByEmail(remoteCustomer.getEmail());
                if (existingCustomer.isPresent()) {
                    // Customer exists, update the details
                    Customer localCustomer = existingCustomer.get();
                    localCustomer.setFirstName(remoteCustomer.getFirstName());
                    localCustomer.setLastName(remoteCustomer.getLastName());
                    localCustomer.setState(remoteCustomer.getState());
                    localCustomer.setStreet(remoteCustomer.getStreet());
                    localCustomer.setCity(remoteCustomer.getCity());
                    localCustomer.setEmail(remoteCustomer.getEmail());
                    localCustomer.setPhone(remoteCustomer.getPhone());

                    customerRepository.save(localCustomer);
                } else {
                    // If customer does not exist, insert into the database
                    customerRepository.save(remoteCustomer);
                }
            }
        }
    }
}
