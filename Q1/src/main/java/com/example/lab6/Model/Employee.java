package com.example.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    @NotNull(message = "id can't be empty")
    @Size(min = 3, message = "id - length must be more than 2 characters.")
    private String id;

    @NotNull(message = "name can't be empty")
    @Size(min = 5, message = "name length must be more than 4 characters.")
    @Pattern(regexp = "([a-zA-Z]+)", message = "name should only contains alphabets characters")
    private String name;

    @Email(message = "it has to be email formated")
    private String email;

    @Pattern(regexp = "^(05)(\\d){8}", message = "phone number should starts with 05 and consist of 10 digits")
    private String phoneNumber;

    @NotNull(message = "age can't be empty")
    @Positive(message = "age must be a positive value")
    @Min(value = 26, message = "Must be more than 25.")
    private int age;

    @NotNull(message = "position can't be empty")
    @Pattern(regexp = "(supervisor|coordinator)")
    private String position;

    @AssertFalse(message = "on leave must be initially set to false")
    private boolean onLeave;

    @NotNull(message = "hire date can't be empty")
    @PastOrPresent(message = "should be a date in the past or the present")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate hireDate;

    @NotNull(message = "annual leave can't be empty")
    @Positive(message = "annual leave should be positive number")
    private int annualLeave;
}
