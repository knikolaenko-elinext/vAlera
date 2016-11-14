package by.knick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import by.knick.valera.ui.Main;

@SpringBootApplication
public class VAleraApplication {

	public static void main(String[] args) {
		SpringApplication.run(VAleraApplication.class, args);
		
		Main.launch(Main.class, args);
	}
}
