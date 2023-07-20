package com.nelcael.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.ManyToAny;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.nelcael.minhasfinancas.model.enuns.StatusLancamento;
import com.nelcael.minhasfinancas.model.enuns.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Table(name = "lancamento", schema="financas")
@Data
public class Lancamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "ano")
	private Integer ano;
	
	@Column(name = "descricao")
	private String descricao;
	
	@ManyToAny
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name = "tipo")
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;
	
}
