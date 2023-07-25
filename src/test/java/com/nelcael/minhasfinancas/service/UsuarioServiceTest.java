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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImplementacao service;
    @MockBean
    UsuarioRespository repository;


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

    @Test
    public void naoDeveAutenticarUsuarioPorJaTerEmailCadastrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@gmail.com", "senha"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Esse e-mail não foi cadastrado");
    }

    @Test
    public void naoDeveAutenticarUsuarioPorTerSenhaErrada() {
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@gmail.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@gmail.com", "123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("A senha é inválida");
    }

    @Test(expected = Test.None.class)
    public void deveSalvarUmUsuario() {
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().nome("usuario").email("email@gmail.com").senha("123").id(1L).build();
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario result = service.salvarUsuario(new Usuario());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(usuario.getId());
        Assertions.assertThat(result.getNome()).isEqualTo(usuario.getNome());
        Assertions.assertThat(result.getEmail()).isEqualTo(usuario.getEmail());
        Assertions.assertThat(result.getSenha()).isEqualTo(usuario.getSenha());
    }

    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUmUsuarioComEmailCadastrado() {
        Usuario usuario = Usuario.builder().email("email@gmail.com").build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(usuario.getEmail());

        service.salvarUsuario(usuario);

        Mockito.verify(repository, Mockito.never()).save(usuario);

    }

}
