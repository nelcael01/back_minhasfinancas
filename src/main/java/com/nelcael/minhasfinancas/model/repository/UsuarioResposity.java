package com.nelcael.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelcael.minhasfinancas.model.entity.Usuario;

public interface UsuarioResposity extends JpaRepository<Usuario, Long>{
	
}
