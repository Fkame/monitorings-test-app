package test.prometheus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в серверное приложение.
 */
@SpringBootApplication
public class PrometheusApplication {

	/**
	 * Точка входа в серверное приложение.
	 * @param args аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(PrometheusApplication.class, args);
	}

}
