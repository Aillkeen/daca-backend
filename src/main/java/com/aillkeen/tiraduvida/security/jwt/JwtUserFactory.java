package com.aillkeen.tiraduvida.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.aillkeen.tiraduvida.entity.Papel;
import com.aillkeen.tiraduvida.entity.Usuario;

public class JwtUserFactory {
	
	private JwtUserFactory() {
    }

    public static JwtUser create(Usuario usuario) {
        return new JwtUser(
        		usuario.getId(),
        		usuario.getEmail(),
                usuario.getPassword(),
                mapToGrantedAuthorities(usuario.getPapel())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Papel grupo) {
    		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
    		authorities.add(new SimpleGrantedAuthority(grupo.toString())); 
    		return authorities;
    }

}
