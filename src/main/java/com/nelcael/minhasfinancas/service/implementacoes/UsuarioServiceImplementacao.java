package com.nelcael.minhasfinancas.service.implementacoes;

import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import com.nelcael.minhasfinancas.service.UsuarioService;
import org.springframework.transaction.annotation.Transactional;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail.");
        }
    }

}
