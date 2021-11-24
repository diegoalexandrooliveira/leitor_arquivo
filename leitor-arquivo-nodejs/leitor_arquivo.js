const fs = require('fs');
const es = require('event-stream');
const { Kafka } = require('kafkajs')

const kafka = new Kafka({
    clientId: 'teste',
    brokers: ['localhost:9092', 'localhost:9093']
})

const producer = kafka.producer()

async function processLineByLine() {
    console.time('teste')
    await producer.connect()

    fs.createReadStream('/home/diegoalexandro/dev/workspace/leitor_arquivo/arquivo.dat')
        .pipe(es.split())
        .pipe(es.mapSync(line => {
            split = line.split('|')
            layout = split[0]
            identificador = split[1]
            ultimoIdentificador = identificador
            if (layout === '001') {
                recebivel = {
                    'identificador': identificador, 'data': split[2], 'cpf': split[3], 'nome': split[4],
                    'agencia': split[5], 'conta': split[6], 'banco': split[7]
                }
                enviaKafka(recebivel)

            } else if (layout === '002') {
                detalhe1 = { 'identificador': identificador, 'hora': split[2], 'valor': split[3], 'modalidade': split[4], 'parcelado': split[5] }
                enviaKafka_detalhe1(detalhe1)
            } else {
                detalhe2 = { 'identificador': identificador, 'produto': split[2], 'quantidade': split[3] }
                enviaKafka_detalhe2(detalhe2)
            }
        }))
        .on('error', err => { })
        .on('end', () => {
            console.timeEnd('teste')
            producer.disconnect()
        });
}

function enviaKafka(recebivel) {
    producer.send({
        topic: 'recebiveis_header',
        messages: [
            { key: undefined, value: JSON.stringify(recebivel) }
        ],
        acks: 0
    })
}

function enviaKafka_detalhe1(detalhe1) {
    producer.send({
        topic: 'recebiveis_detalhe1',
        messages: [
            { key: undefined, value: JSON.stringify(detalhe1) }
        ],
        acks: 0
    })
}

function enviaKafka_detalhe2(detalhe2) {
    producer.send({
        topic: 'recebiveis_detalhe2',
        messages: [
            { key: undefined, value: JSON.stringify(detalhe2) }
        ],
        acks: 0
    })
}

processLineByLine();