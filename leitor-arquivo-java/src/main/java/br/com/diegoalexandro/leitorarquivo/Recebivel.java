package br.com.diegoalexandro.leitorarquivo;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Recebivel {

    private String identificador;

    private String data;

    private String cpf;

    private String nome;

    private String agencia;

    private String conta;

    private String banco;
}
