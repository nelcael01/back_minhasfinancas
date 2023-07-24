package com.nelcael.minhasfinancas.service;

import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import com.nelcael.minhasfinancas.service.implementacoes.UsuarioServiceImplementacao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;
    UsuarioRespository repository;

    @Before
    public void setUp() {
        repository = Mockito.mock(UsuarioRespository.class);
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

//    @Test(expected = RegraNegocioException.class)
//    public void naoDeveSalvarUsuarioPorJaTerEmailCadastrado(){
//        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
//        repository.save(usuario);
//
//        Usuario usuario2 = Usuario.builder().nome("usuario").email("usuario@email.com").build();
//        repository.save(usuario2);
//    }


}
