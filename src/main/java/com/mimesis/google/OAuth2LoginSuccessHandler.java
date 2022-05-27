package com.mimesis.google;

import com.mimesis.controller.UsuarioController;
import com.mimesis.entity.Usuario;
import com.mimesis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Hola");
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByCorreo(customOAuth2User.getEmail());
        String email= customOAuth2User.getEmail();
        String nombre= customOAuth2User.getFirstName();
        String apellido=customOAuth2User.getLastName();
        Boolean verified=customOAuth2User.getVerification();
        System.out.println("email: "+email);
        System.out.println("nombre: "+nombre);
        System.out.println("apellido: "+apellido);
        System.out.println("verified: "+verified);
        if(usuario == null){
            usuario =  new Usuario(nombre,apellido,email,"Cliente",verified,"GOOGLE");
            usuario.setToken(null);
            usuario.setNumerotelefonico("1");
            usuario.setDireccion("1");
            usuario.setDni(1);
            usuario.setFechanacimiento(LocalDate.now().minusDays(1));
            System.out.println("valido: "+ usuario.getValido());
            usuarioRepository.save(usuario);
        }else{
            System.out.println("HOLA CAUSA");
            //update user
            System.out.println("Updatea customer");
            usuario.setToken(null);
            usuario.setNombre(nombre);
            usuario.setAuthprovider("GOOGLE");
            usuario.setApellido(apellido);
            usuarioRepository.save(usuario);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
