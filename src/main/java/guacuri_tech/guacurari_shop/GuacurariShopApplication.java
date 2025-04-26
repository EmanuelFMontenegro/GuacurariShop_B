package guacuri_tech.guacurari_shop;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class GuacurariShopApplication {
	public static void main(String[] args) {
		// Cargar variables de entorno
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);

		// Obtener la fecha y hora actual en la zona horaria de Buenos Aires
		LocalDateTime fechaHora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

		// formateador con el formato: "d/M/yyyy 'hora' HH:mm"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'hora' HH:mm");

		// Formatear la fecha
		String fechaFormateada = fechaHora.format(formatter);

		// Iniciar la aplicación Spring Boot
		SpringApplication.run(GuacurariShopApplication.class, args);
	}
}
