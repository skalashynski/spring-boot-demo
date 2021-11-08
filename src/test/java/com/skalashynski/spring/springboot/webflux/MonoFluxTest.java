package com.skalashynski.spring.springboot.webflux;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoFluxTest {

    @Test
    public void testMono() {
        Mono<String> monoString = Mono.just("Hello world");
        monoString.subscribe(System.out::println);
    }

    @Test
    public void testMonoAndPrintEachExecution() {
        Mono<String> monoString = Mono.just("Hello world").log();
        monoString.subscribe(System.out::println);
    }

    @Test
    public void testMonoAndThrowingException() {
        Mono<?> monoString = Mono.just("Hello world")
            .then(Mono.error(new RuntimeException("Exception is occured")))
            .log();
        monoString.subscribe(System.out::println);
    }

    @Test
    public void testMonoAndThrowingExceptionAndCatching() {
        Mono<?> monoString = Mono.just("Hello world")
            .then(Mono.error(new RuntimeException("Exception is occured in Mono")))
            .log();
        monoString.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }

    @Test
    public void testFlux() {
        Flux<?> flux = Flux.just("Spring", "Spring Boot", "Hibernate", "microservices")
            .concatWithValues("AWS")
            .concatWith(Flux.error(new RuntimeException("Exception occured in Flux")))
            .concatWithValues("GCP")
            .concatWithValues("Azure")
            .log();

        flux.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }
}
