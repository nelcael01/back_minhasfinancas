package com.nelcael.minhasfinancas.service;

import com.nelcael.minhasfinancas.exceptions.ErroAutenticacao;
import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import com.nelcael.minhasfinancas.service.implementacoes.UsuarioServiceImplementacao;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;
    @MockBean
    UsuarioRespository repository;

    @Before
    public void setUp() {
        service = new UsuarioServiceImplementacao(repository);
    }

    @Test(expected = Test.None.class)
    public void deveValidarEmail() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail("usuario@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveRetonarExceptionPorJaTerEmail() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        service.validarEmail("usuario@email.com");
    }

    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso() {
        String email = "email@gmail.com";
        String senha = "senha";
        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();
    }

    @Test(expected = ErroAutenticacao.class)
    public void naoDeveAutenticarUsuarioPorJaTerEmailCadastrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        service.autenticar("email@gmail.com", "senha");
    }

    @Test(expected = ErroAutenticacao.class)
    public void naoDeveAutenticarUsuarioPorTerSenhaErrada(){
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@gmail.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        service.autenticar("email@gmail.com","123");
    }


}
