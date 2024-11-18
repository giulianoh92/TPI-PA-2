package com.tpi.tpi.config;

import com.tpi.tpi.common.service.CustomerService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

// esta clase es para la autenticación de los usuarios
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;

    // inyectamos el servicio de los clientes
    public CustomAuthenticationProvider(CustomerService customerService) {
        this.customerService = customerService;
    }

    // método para autenticar a los usuarios
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // si el usuario y la contraseña son correctos, se devuelve un token de autenticación
        if (customerService.authenticate(email, password)) {
            return new UsernamePasswordAuthenticationToken(email, password, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        } else {
            return null;
        }
    }
    
    // método para verificar si el objeto de autenticación es compatible con la clase de autenticación
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}