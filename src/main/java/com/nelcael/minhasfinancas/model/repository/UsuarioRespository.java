package com.nelcael.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelcael.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioRespository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
