package br.com.diegoalexandro.leitorarquivo;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Detalhe2 {

    private String identificador;

    private String produto;

    private Integer quantidade;
}
