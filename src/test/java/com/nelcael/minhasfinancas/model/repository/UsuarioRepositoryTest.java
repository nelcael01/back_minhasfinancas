package com.nelcael.minhasfinancas.model.repository;

import com.nelcael.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRespository respository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //Cenário
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        entityManager.persist(usuario);

        //Ação
        boolean resultado = respository.existsByEmail(usuario.getEmail());

        //Verificação
        Assertions.assertThat(resultado).isTrue();
    }

    @Test
    public void deveRetonarFalsoQuandoNaoHouverUsuarioCadastroComOEmail(){
        //Ação
        boolean result = respository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isFalse();
    }


}
