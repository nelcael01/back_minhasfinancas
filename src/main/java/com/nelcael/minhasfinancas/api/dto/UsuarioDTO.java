package com.nelcael.minhasfinancas.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
}
