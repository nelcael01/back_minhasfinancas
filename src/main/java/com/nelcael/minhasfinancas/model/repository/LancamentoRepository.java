package com.nelcael.minhasfinancas.model.repository;

import com.nelcael.minhasfinancas.model.enuns.TipoLancamento;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nelcael.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

    Lancamento save(Lancamento lancamento);
    void delete(Lancamento lancamento);
    Optional<Lancamento> findById(Long id);
    List<Lancamento> findAll(Example example);

    @Query(value = "select sum(l.valor) from Lancamento l join l.usuario u" +
            " where u.id = :idUsuario and l.tipo =:tipo group by u")
    BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);
}
