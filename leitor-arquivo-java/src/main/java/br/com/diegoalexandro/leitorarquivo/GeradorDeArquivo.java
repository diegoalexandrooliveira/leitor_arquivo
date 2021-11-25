package br.com.diegoalexandro.leitorarquivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GeradorDeArquivo {

    public static void main(String[] args) throws IOException {

        // IDENTIFICADOR | DATA | CPF | NOME | AG | CONTA | BANCO
        String cabecalho = "001|%s|%s|%s|%s|%s|%s|%s";

        //  IDENTIFICADOR | HORA | VALOR | MODALIDADE | PARCELADO
        String detalhe1 = "002|%s|%s|%s|%s|%s";

        //  IDENTIFICADOR | PRODUTO | QUANTIDADE
        String detalhe2 = "003|%s|%s|%s";
        String dataLocal = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);

        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/diegoalexandro/dev/workspace/leitor_arquivo/arquivo_10k.dat"));
        for (int i = 0; i < 10_000; i++) {
            System.out.println(i);
            escreveLinha(cabecalho, detalhe1, detalhe2, dataLocal, writer);
        }
        writer.close();
    }

    private static void escreveLinha(String cabecalho, String detalhe1, String detalhe2, String dataLocal, BufferedWriter writer) throws IOException {
        String identificador = UUID.randomUUID().toString();
        String linhaCabecalho = String.format(cabecalho, identificador, dataLocal, "111.111.111/11", "João", "0005", "0054544545", "C6 Bank");

        writer.write(linhaCabecalho);
        int detalhes1 = ThreadLocalRandom.current().nextInt(1, 10);
        for (int i = 0; i < detalhes1; i++) {
            String linhaDetalhe1 = String.format(detalhe1, identificador, LocalDateTime.now().format(DateTimeFormatter.ISO_TIME), ThreadLocalRandom.current().nextDouble(1, 10000), "CREDITO", "12x");
            writer.write("\n");
            writer.write(linhaDetalhe1);
        }
        int detalhes2 = ThreadLocalRandom.current().nextInt(1, 10);
        for (int i = 0; i < detalhes2; i++) {
            String linhaDetalhe2 = String.format(detalhe2, identificador, "PRODUTO TESTE", 10);
            writer.write("\n");
            writer.write(linhaDetalhe2);
        }
        writer.write("\n");
    }
}
