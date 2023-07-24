package com.nelcael.minhasfinancas.model.repository;

import com.nelcael.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRespository respository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //Cenário
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        respository.save(usuario);

        //Ação
        boolean resultado = respository.existsByEmail(usuario.getEmail());

        //Verificação
        Assertions.assertThat(resultado).isTrue();
    }
}
