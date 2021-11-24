package br.com.diegoalexandro.leitorarquivo;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Detalhe1 {

    private String identificador;

    private String hora;

    private Double valor;

    private String modalidade;

    private String parcelado;
}
