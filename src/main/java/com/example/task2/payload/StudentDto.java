package com.example.task2.payload;

import lombok.Data;

import java.util.List;
@Data
public class StudentDto{
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;
    private List<Integer> subjects;
}
