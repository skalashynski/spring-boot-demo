package com.skalashynski.spring.springboot.repository;

import com.skalashynski.spring.springboot.entity.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class CustomerRepository {

    private static void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getCustomers() {
        return LongStream.rangeClosed(1, 10)
            .peek(e -> sleep(1000))
            .peek(e -> System.out.println("Processing i: " + e))
            .mapToObj(i -> new Customer(i, "Name: " + i))
            .collect(Collectors.toList());
    }

    public Flux<Customer> getCustomersStream() {
        return Flux.range(1, 10)
            .delayElements(Duration.ofMillis(1000))
            .doOnNext(e -> System.out.println("Processing count in stream flow. I: " + e))
            .map(i -> new Customer(i.longValue(), "Name: " + i));
    }

}
