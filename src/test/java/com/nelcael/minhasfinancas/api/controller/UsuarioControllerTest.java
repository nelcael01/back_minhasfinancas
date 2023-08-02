package com.nelcael.minhasfinancas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelcael.minhasfinancas.api.dto.UsuarioDTO;
import com.nelcael.minhasfinancas.exceptions.ErroAutenticacao;
import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.service.LancamentoService;
import com.nelcael.minhasfinancas.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;
    @Autowired
    MockMvc mvc;
    @MockBean
    UsuarioService service;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        String email = "email@gmail.com";
        String senha = "senha";
        UsuarioDTO dto = UsuarioDTO.builder().nome("Nelcael").email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
        Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveLancarUmErroAoAutenticarUmUsuario() throws Exception {
        String email = "email@gmail.com";
        String senha = "senha";
        UsuarioDTO dto = UsuarioDTO.builder().nome("Nelcael").email(email).senha(senha).build();
        Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveSalvarUmUsuario() throws Exception {
        UsuarioDTO dto = UsuarioDTO.builder().nome("Nelcael").email("email@gmail.com").senha("senha").build();
        Usuario usuarioSalvo = Usuario.builder().id(1l).email("email@gmail.com").senha("senha").build();
        Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioSalvo);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioSalvo.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioSalvo.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioSalvo.getEmail()));
    }

    @Test
    public void deveRetornarBadRequestAoTentarSalvarUmUsuario() throws Exception {
        UsuarioDTO dto = UsuarioDTO.builder().nome("Nelcael").email("email@gmail.com").senha("senha").build();
        Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
