package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.entity.Employee;
import com.example.demo.domain.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	public void whenFindByName_thenReturnEmployee() {
		//insert dummy data in database
		Employee emp1 = createEmployee("Shubham Kumar", "IT Analyst");

		// when
		Employee found = employeeRepository.findByName(emp1.getName());

		// then
		assertThat(found.getName()).isEqualTo(emp1.getName());
	}
	
	@Test
	public void whenFindByNameWrong_thenReturnNull() {

		// when data does not exist
		Employee found = employeeRepository.findByName("Test1");

		// then
		assertThat(found == null);
	}

	private Employee createEmployee(String name, String role) {
		Employee employee = new Employee();
		employee.setName(name);
		employee.setRole(role);
		entityManager.persist(employee);
		entityManager.flush();
		return employee;
	}

}