package br.com.diegoalexandro.leitorarquivo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LeitorArquivo implements ApplicationRunner {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            long inicio = System.currentTimeMillis();
            final LineIterator lineIterator = FileUtils.lineIterator(new File("/home/diegoalexandro/dev/workspace/leitor_arquivo/arquivo_10k.dat"));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split("\\|");
                String layout = split[0];
                String identificador = split[1];
                if (layout.equals("001")) {
                    Recebivel recebivel = Recebivel.builder()
                            .identificador(identificador)
                            .data(split[2])
                            .cpf(split[3])
                            .nome(split[4])
                            .agencia(split[5])
                            .conta(split[6])
                            .banco(split[7])
                            .build();
                    enviaKafka(recebivel);
                } else if (layout.equals("002")) {
                    Detalhe1 detalhe1 = Detalhe1.builder()
                            .identificador(identificador)
                            .hora(split[2])
                            .valor(Double.parseDouble(split[3]))
                            .modalidade(split[4])
                            .parcelado(split[5]).build();
                    enviaKafka(detalhe1);
                } else {
                    Detalhe2 detalhe2 = Detalhe2.builder()
                            .identificador(identificador)
                            .produto(split[2])
                            .quantidade(Integer.parseInt(split[3]))
                            .build();
                    enviaKafka(detalhe2);
                }
            }
            System.out.println(System.currentTimeMillis() - inicio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviaKafka(Recebivel recebivel) throws JsonProcessingException {
        kafkaTemplate.send("recebiveis_header", objectMapper.writeValueAsString(recebivel));
    }

    private void enviaKafka(Detalhe1 detalhe1) throws JsonProcessingException {
        kafkaTemplate.send("recebiveis_detalhe1", objectMapper.writeValueAsString(detalhe1));
    }

    private void enviaKafka(Detalhe2 detalhe2) throws JsonProcessingException {
        kafkaTemplate.send("recebiveis_detalhe2", objectMapper.writeValueAsString(detalhe2));
    }

}
