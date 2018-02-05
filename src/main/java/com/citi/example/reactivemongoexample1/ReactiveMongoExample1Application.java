package com.citi.example.reactivemongoexample1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveMongoExample1Application {

	/*@Bean
	CommandLineRunner employee(EmployeeRepository employeeRepository)
	{
		return args -> employeeRepository
				.deleteAll()
				.subscribe(null,null,() ->
				{
					Stream.of( new Employee(UUID.randomUUID().toString(),
							"Peter",23000L),
							new Employee(UUID.randomUUID().toString(),
							"John",25000L),
							new Employee(UUID.randomUUID().toString(),
									"Amy",27000L)
					).forEach(employee -> {
						try {
							Thread.sleep(3000);
						}catch(InterruptedException e){

						}
						employeeRepository.save(employee)
								.subscribe(System.out::println);
					});

	});
	}*/
	public static void main(String[] args) {
		SpringApplication.run(ReactiveMongoExample1Application.class, args);
	}
}



























