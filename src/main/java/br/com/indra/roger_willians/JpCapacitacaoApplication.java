package br.com.indra.roger_willians;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JpCapacitacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpCapacitacaoApplication.class, args);
	}

}
