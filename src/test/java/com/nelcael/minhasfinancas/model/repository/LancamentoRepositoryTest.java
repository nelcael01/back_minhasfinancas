package com.nelcael.minhasfinancas.model.repository;

import com.nelcael.minhasfinancas.model.entity.Lancamento;
import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.enuns.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {
    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamento = criarLancamento();
        lancamento = repository.save(lancamento);
        Assertions.assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento(){
        Lancamento lancamento = criarLancamento();
        Lancamento lancamentoSalvo = entityManager.persist(lancamento);
        lancamentoSalvo = entityManager.find(Lancamento.class, lancamentoSalvo.getId());
        repository.delete(lancamentoSalvo);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamentoSalvo.getId());
        Assertions.assertThat(lancamentoInexistente).isNull();
    }

     private Lancamento criarLancamento(){
         return Lancamento.builder()
                 .ano(2019)
                 .mes(1)
                 .descricao("Lancamento qualquer")
                 .valor(BigDecimal.valueOf(10))
                 .tipo(TipoLancamento.RECEITA)
                 .status(StatusLancamento.PENDENTE)
                 .dataCadastro(LocalDate.now())
                 .build();
     }


}
