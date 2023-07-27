package com.nelcael.minhasfinancas.api.controller;

import com.nelcael.minhasfinancas.api.dto.LancamentoDTO;
import com.nelcael.minhasfinancas.exceptions.RegraNegocioException;
import com.nelcael.minhasfinancas.model.entity.Lancamento;
import com.nelcael.minhasfinancas.model.entity.Usuario;
import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.enuns.TipoLancamento;
import com.nelcael.minhasfinancas.service.LancamentoService;
import com.nelcael.minhasfinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {
    private LancamentoService service;
    private UsuarioService usuarioService;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        return service.buscarPorId(id).map(entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrato na base", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return service.buscarPorId(id).map(entity -> {
            service.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> {
            return new ResponseEntity("Lançamento não encontrado na base", HttpStatus.BAD_REQUEST);
        });
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado");
        }else {
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> listBuscados = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok().body(listBuscados);
    }

    private Lancamento converter(LancamentoDTO dto) {
        Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado"));
        Lancamento lancamento = Lancamento.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .mes(dto.getMes())
                .ano(dto.getAno())
                .valor(dto.getValor())
                .usuario(usuario)
                .tipo(TipoLancamento.valueOf(dto.getTipo()))
                .status(StatusLancamento.valueOf(dto.getStatus()))
                .build();
        return lancamento;
    }
}
