package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	public Employee findByName(String name);

}