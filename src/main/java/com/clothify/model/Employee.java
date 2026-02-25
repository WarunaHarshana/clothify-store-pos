package com.clothify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int employeeId;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String hireDate;
    private boolean active;
}

