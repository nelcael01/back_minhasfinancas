package com.nelcael.minhasfinancas.service.implementacoes;

import com.nelcael.minhasfinancas.model.entity.Lancamento;
import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.repository.LancamentoRepository;
import com.nelcael.minhasfinancas.service.LancamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class LancamentoServiceImplementacao implements LancamentoService {

    private LancamentoRepository repository;
    public LancamentoServiceImplementacao(LancamentoRepository repository){
        this.repository = repository;
    }
    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        return repository.save(lancamento);
    }

    @Override
    public Lancamento atualizar(Lancamento lancamento) {
        return null;
    }

    @Override
    public void deletar(Lancamento lancamento) {

    }

    @Override
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        return null;
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {

    }
}
