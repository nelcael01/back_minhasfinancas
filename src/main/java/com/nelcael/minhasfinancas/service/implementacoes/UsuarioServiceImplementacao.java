package com.nelcael.minhasfinancas.service.implementacoes;

import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.repository.UsuarioRespository;
import com.nelcael.minhasfinancas.service.UsuarioService;

public class UsuarioServiceImplementacao implements UsuarioService{

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
		// TODO Auto-generated method stub
		
	}
	
}
