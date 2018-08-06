package com.aillkeen.assistencia.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.aillkeen.assistencia.entity.Papel;
import com.aillkeen.assistencia.entity.Usuario;

public class JwtUserFactory {
	
	private JwtUserFactory() {
    }

    public static JwtUser create(Usuario usuario) {
        return new JwtUser(
        		usuario.getId(),
        		usuario.getEmail(),
                usuario.getSenha(),
                mapToGrantedAuthorities(usuario.getPapel())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Papel grupo) {
    		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
    		authorities.add(new SimpleGrantedAuthority(grupo.toString())); 
    		return authorities;
    }

}
