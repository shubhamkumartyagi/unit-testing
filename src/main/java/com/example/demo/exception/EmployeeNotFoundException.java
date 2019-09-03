package com.example.demo.exception;

public class EmployeeNotFoundException extends RuntimeException {

	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = 7180073910267366961L;

	public EmployeeNotFoundException(Long id) {
		super("Could not find employee " + id);
	}
}
