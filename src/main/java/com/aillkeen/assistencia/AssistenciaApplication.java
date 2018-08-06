package com.aillkeen.assistencia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aillkeen.assistencia.entity.Papel;
import com.aillkeen.assistencia.entity.Usuario;
import com.aillkeen.assistencia.repository.UsuarioRepository;

@SpringBootApplication
public class AssistenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistenciaApplication.class, args);
	}
	
	 @Bean
	 CommandLineRunner init(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
	        return args -> {
	            initUsers(userRepository, passwordEncoder);
	        };

	    }
	    
		private void initUsers(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
			Usuario admin = new Usuario();
	        admin.setEmail("admin@assistencia.com");
	        admin.setSenha(passwordEncoder.encode("123456"));	
	        admin.setPapel(Papel.ROLE_PROFESSOR);

	        Usuario find = userRepository.findByEmail("admin@assistencia.com");
	        if (find == null) {
	            userRepository.save(admin);
	        }
	    }
}
