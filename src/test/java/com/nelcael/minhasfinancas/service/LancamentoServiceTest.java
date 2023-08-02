package com.nelcael.minhasfinancas.service;

import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Lancamento;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.enuns.TipoLancamento;
import com.nelcael.minhasfinancas.model.repository.LancamentoRepository;
import com.nelcael.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.nelcael.minhasfinancas.service.implementacoes.LancamentoServiceImplementacao;
import org.assertj.core.api.Assertions;
import org.hibernate.criterion.Example;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamento);

        Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), RegraNegocioException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarLancamento() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        service.deletar(lancamento);

        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void naoDeveDeletarUmLancamentoQueNãoFoiSalvo() {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

//    @Test
//    public void deveFiltrarLancamento() {
//        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
//        lancamento.setId(1l);
//
//        List<Lancamento> lista = Arrays.asList(lancamento);
//        Mockito.when( repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
//
//        List<Lancamento> resultado = service.buscar(lancamento);
//        Assertions.assertThat(resultado).isNotEmpty();
//    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        service.atualizarStatus(lancamento, novoStatus);

        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);
    }

    @Test
    public void deveObterUmLancamentoBuscandoPorId(){
        Long id = 1l;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        Optional<Lancamento> resultado = service.buscarPorId(id);

        Assertions.assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveRetonarVazioNaBuscaPorIdDeLancamento(){
        Long id = 1l;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Lancamento> resultado = service.buscarPorId(id);

        Assertions.assertThat(resultado.isPresent()).isFalse();
    }
    @Test
    public void deveRetonarUmaExceptionNaValidacao(){
        Lancamento lancamento = new Lancamento();

        Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida");

        lancamento.setDescricao("");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida");

        lancamento.setDescricao("Salário");

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");

        lancamento.setMes(-1);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");

        lancamento.setMes(13);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");

        lancamento.setMes(5);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");

        lancamento.setAno(20222);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");

        lancamento.setAno(2022);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido");

        lancamento.setUsuario(new Usuario());

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido");

        lancamento.getUsuario().setId(1l);

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");

        lancamento.setValor(BigDecimal.valueOf(0));

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");

        lancamento.setValor(BigDecimal.valueOf(2000));

        erro = Assertions.catchThrowable(() -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lançamento");

        lancamento.setTipo(TipoLancamento.RECEITA);
    }
}
