package com.skalashynski.spring.springboot.util;

import com.skalashynski.spring.springboot.entity.Student;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class StudentServiceTestArgsProvider {

    public static Stream<Student> provideStudents() {
        return Stream.of(
                new Student(Long.MIN_VALUE, "firstName", "lastName",
                        LocalDate.of(1993, 1, 17), LocalDateTime.now()),
                new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()),
                new Student(1L, "", "",
                        LocalDate.of(1980, 5, 28), LocalDateTime.now()),
                new Student(20L, "Ivan", null,
                        LocalDate.of(2005, 8, 10), LocalDateTime.now())
        );
    }

    private static Stream<Arguments> provideArgsForGetByIdTest() {
        return Stream.of(
                arguments(Long.MIN_VALUE),
                arguments(Long.MAX_VALUE),
                arguments(20L),
                arguments(1234L)
        );
    }

    private static Stream<Arguments> provideArgsForFindByFirstNameTest() {
        return Stream.of(
                arguments("someName"),
                arguments(""),
                arguments("Ivan")
        );
    }

    private static Stream<Arguments> provideArgsForDeleteOkTest() {
        return Stream.of(
                arguments(Long.MIN_VALUE),
                arguments(Long.MAX_VALUE),
                arguments(20L)
        );
    }

    private static Stream<Arguments> provideArgsForDeleteFailTest() {
        return Stream.of(
                arguments(5L),
                arguments(100L),
                arguments(1234L)
        );
    }

    private static Stream<Arguments> provideArgsForUpdateTest() {
        return Stream.of(
                arguments(1L, new Student(20L, "Ivan", null,
                        LocalDate.of(2000, 1, 1), LocalDateTime.now())),
                arguments(Long.MIN_VALUE, new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()))
        );
    }

    private static Stream<Arguments> provideArgsForUpdateFailTest() {
        return Stream.of(
                arguments(111L, new Student(20L, "Ivan", null,
                        LocalDate.of(2000, 1, 1), LocalDateTime.now())),
                arguments(3L, new Student(Long.MAX_VALUE, "someName", "lastName",
                        LocalDate.of(2000, 12, 1), LocalDateTime.now()))
        );
    }

    private static Stream<Arguments> provideArgsForFindBetweenBirthdaysTest() {
        return Stream.of(
                // from 01.01.1985 to 10.08.2005
                arguments(new Date(473385600000L), new Date(1123632000000L)),
                // from 01.12.2000 to 01.01.2006
                arguments(new Date(975628800000L), new Date(1136073600000L))
        );
    }
}
