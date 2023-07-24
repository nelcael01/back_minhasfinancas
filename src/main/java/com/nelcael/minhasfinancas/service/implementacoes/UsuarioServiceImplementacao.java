package com.nelcael.minhasfinancas.service.implementacoes;

import com.nelcael.minhasfinancas.exceptions.ErroAutenticacao;
import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import com.nelcael.minhasfinancas.service.UsuarioService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImplementacao implements UsuarioService {
    @Autowired
    private UsuarioRespository repository;

    public UsuarioServiceImplementacao(UsuarioRespository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (!usuario.isPresent()) {
            throw new ErroAutenticacao("Esse e-mail não foi cadastrado");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacao("A senha é inválida");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail.");
        }
    }

}
