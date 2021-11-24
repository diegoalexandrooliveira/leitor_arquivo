import json
import time

from kafka import KafkaProducer

from Detalhe1 import Detalhe1
from Detalhe2 import Detalhe2
from Recebivel import Recebivel

producer = KafkaProducer(bootstrap_servers=['localhost:9092', 'localhost:9093'],
                         value_serializer=lambda m: json.dumps(m).encode('utf-8'))


def leArquivo(caminho):
    inicio = time.time()
    with open(caminho) as infile:
        for line in infile:
            split = line.split('|')
            layout = split[0]
            identificador = split[1]
            if layout == '001':
                recebivel = Recebivel(identificador=identificador, data=split[2], cpf=split[3], nome=split[4],
                                      agencia=split[5], conta=split[6], banco=split[7])
                envia_kafka_recebivel(recebivel)
            elif layout == '002':
                detalhe1 = Detalhe1(identificador=identificador, hora=split[2], valor=split[3], modalidade=split[4],
                                    parcelado=split[5])
                envia_kafka_detalhe1(detalhe1)
            elif layout == '003':
                detalhe2 = Detalhe2(identificador=identificador, produto=split[2], quantidade=split[3])
                envia_kafka_detalhe2(detalhe2)
        print(time.time() - inicio)
        producer.flush()


def envia_kafka_recebivel(recebivel):
    producer.send("recebiveis_header", recebivel.__dict__)


def envia_kafka_detalhe1(detalhe1):
    producer.send("recebiveis_detalhe1", detalhe1.__dict__)


def envia_kafka_detalhe2(detalhe2):
    producer.send("recebiveis_detalhe2", detalhe2.__dict__)


leArquivo('/home/diegoalexandro/dev/workspace/leitor_arquivo/arquivo.dat')
