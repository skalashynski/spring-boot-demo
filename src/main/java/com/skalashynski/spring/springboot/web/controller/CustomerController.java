package com.skalashynski.spring.springboot.web.controller;

import com.skalashynski.spring.springboot.entity.Customer;
import com.skalashynski.spring.springboot.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public List<Customer> getCustomers() {
        return customerService.loadAllCustomers();
    }

    @GetMapping(value = "/stream", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getCustomersStream() {
        return customerService.loadAllCustomersStream();
    }

}
