package com.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.EmployeeController;
import com.example.demo.domain.entity.Employee;
import com.example.demo.domain.repository.EmployeeRepository;
import com.example.demo.exception.EmployeeNotFoundAdvice;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeControllerTest.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeRepository repository;

	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup(new EmployeeController(repository))
				.setControllerAdvice(new EmployeeNotFoundAdvice())
				.build();
	}

	@Test
	public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {

		Employee emp1 = createEmployee("Shubham Kumar", "IT Analyst");
		Employee emp2 = createEmployee("Asif T", "IT Consultant");

		List<Employee> allEmployees = Arrays.asList(emp1, emp2);

		when(repository.findAll()).thenReturn(allEmployees);

		mvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name", is(emp1.getName())));
	}

	@Test
	public void givenEmployee_whenGetEmployee_thenReturnJson() throws Exception {

		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("name", "Shubham Kumar");
		jsonRequest.put("role", "IT Analyst");

		Employee emp = createEmployee("Shubham Kumar", "IT Analyst");

		when(repository.save(emp)).thenReturn(emp);

		mvc.perform(post("/employees").content(jsonRequest.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(jsonRequest.getString("name"))));
	}
	
	@Test
	public void givenEmployeeId_whenEmployeeFound_thenReturnEmployee() throws Exception {
		
		Employee emp1 = createEmployee("Shubham Kumar", "IT Analyst");
		emp1.setId((long) 10);

		when(repository.findById((long) 10)).thenReturn(Optional.of(emp1));

		mvc.perform(get("/employees/10").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(emp1.getId().intValue())));
	}
	
	@Test
	public void givenEmployeeId_whenEmployeeNotFound_thenReturnException() throws Exception {

		mvc.perform(get("/employees/100").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", is("Could not find employee 100")));
	}
	
	private Employee createEmployee(String name, String role) {
		Employee emp = new Employee();
		emp.setName(name);
		emp.setRole(role);
		return emp;
	}

}
