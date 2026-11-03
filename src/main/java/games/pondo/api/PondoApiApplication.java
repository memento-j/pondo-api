package games.pondo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@RestController
@EnableCaching 
public class PondoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PondoApiApplication.class, args);
	}

	@GetMapping
	public String tester() {
		return "Pondo Games API :)";
	}
	
}
