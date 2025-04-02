package guacuri_tech.guacurari_shop;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GuacurariShopApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();


		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);

		SpringApplication.run(GuacurariShopApplication.class, args);
	}
}

