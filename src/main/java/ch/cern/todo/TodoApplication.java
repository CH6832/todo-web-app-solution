package ch.cern.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Todo REST API application.
 * This class serves as the entry point and configuration hub for the Spring Boot application.
 */
@SpringBootApplication(scanBasePackages = "ch.cern.todo")
@EntityScan("ch.cern.todo.model")
@EnableJpaRepositories("ch.cern.todo.repository")
public class TodoApplication {

	/**
	 * Main method which serves as the entry point for the Spring Boot application.
	 * Initializes the Spring application context and starts the embedded web server.
	 *
	 * The application will:
	 * - Initialize the Spring context
	 * - Set up the embedded Tomcat server
	 * - Configure the H2 database
	 * - Initialize security configurations
	 * - Start the REST API endpoints
	 */
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}
}
