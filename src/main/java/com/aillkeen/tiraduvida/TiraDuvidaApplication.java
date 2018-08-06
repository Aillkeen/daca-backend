package com.aillkeen.tiraduvida;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aillkeen.tiraduvida.entity.Papel;
import com.aillkeen.tiraduvida.entity.Usuario;
import com.aillkeen.tiraduvida.repository.UsuarioRepository;

@SpringBootApplication
public class TiraDuvidaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiraDuvidaApplication.class, args);
	}
	
	 @Bean
	 CommandLineRunner init(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
	        return args -> {
	            initUsers(userRepository, passwordEncoder);
	        };

	    }
	    
		private void initUsers(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
			Usuario admin = new Usuario();
	        admin.setEmail("professor@tiraduvida.com");
	        admin.setPassword(passwordEncoder.encode("123456"));	
	        admin.setPapel(Papel.ROLE_PROFESSOR);

	        Usuario find = userRepository.findByEmail("professor@tiraduvida.com");
	        if (find == null) {
	            userRepository.save(admin);
	        }
	    }
}
