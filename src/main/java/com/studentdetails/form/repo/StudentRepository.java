package com.studentdetails.form.repo;

import java.util.List;

import com.studentdetails.form.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
	
	List<Student> findByLastNameStartsWithIgnoreCase(String lastName);
	
	List<Student> findByFirstNameStartsWithIgnoreCase(String firstName);

}
