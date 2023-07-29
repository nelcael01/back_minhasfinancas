package com.nelcael.minhasfinancas.service;

import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Lancamento;
import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.repository.LancamentoRepository;
import com.nelcael.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.nelcael.minhasfinancas.service.implementacoes.LancamentoServiceImplementacao;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImplementacao service;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento() {
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        Lancamento lancamento = service.salvar(lancamentoASalvar);

        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

        Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        Mockito.doNothing().when(service).validar(lancamentoSalvo);
        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        Lancamento lancamento = service.atualizar(lancamentoSalvo);

        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void naoDeveAtualizarUmLancamentoQueNãoFoiSalvo() {
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();

        Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoSalvo), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvo);
    }

    @Test
    public void naoDeveAtualizarUmLancamentoQuandoHouverErroDeValidacao() {
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvo);

        Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoSalvo), RegraNegocioException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvo);
    }


}
