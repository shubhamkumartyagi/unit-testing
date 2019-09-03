package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.domain.entity.Employee;
import com.example.demo.domain.repository.EmployeeRepository;

@Configuration
@Slf4j
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(createDatabae("Bilbo Baggins", "burglar")));
      log.info("Preloading " + repository.save(createDatabae("Frodo Baggins", "thief")));
    };
   
  }
  
  Employee createDatabae(String name, String role) {
  	Employee emp = new Employee();
  	emp.setName(name);
  	emp.setRole(role);
  	return emp;
  }
}
