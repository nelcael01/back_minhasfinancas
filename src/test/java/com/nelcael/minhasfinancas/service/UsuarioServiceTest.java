package com.nelcael.minhasfinancas.service;

import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRespository repository;

    @Test(expected = Test.None.class)
    public void deveValidarEmail() {
        repository.deleteAll();
        service.validarEmail("usuario@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveRetonarExceptionPorJaTerEmail() {
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        repository.save(usuario);
        service.validarEmail(usuario.getEmail());
    }

//    @Test(expected = RegraNegocioException.class)
//    public void naoDeveSalvarUsuarioPorJaTerEmailCadastrado(){
//        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
//        repository.save(usuario);
//
//        Usuario usuario2 = Usuario.builder().nome("usuario").email("usuario@email.com").build();
//        repository.save(usuario2);
//    }


}
