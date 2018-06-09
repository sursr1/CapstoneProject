package com.studentdetails.form;

import static org.junit.Assert.assertEquals;

import com.studentdetails.form.repo.StudentRepository;
import com.studentdetails.form.repo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SampleVaadinApplicationTests {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void shouldStudentsFillOutComponentsWithDataWhenAppStart(){
		assertEquals(5, this.studentRepository.count());
	}
	
	@Test
	public void shouldUsersFillOutComponentsWithDataWhenAppStart(){
		assertEquals(2, this.userRepository.count());
	}
	
	@Test
	public void shouldFindTwoBaurerStudents(){
		assertEquals(5, this.studentRepository.findByFirstNameStartsWithIgnoreCase("Baurer"));
	}
	
}
