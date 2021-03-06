# Anotações de resumo

* Java RMI
* Três servidores: coordenador, hotel, companhia aérea
* Os clientes se comunicam apenas com o servidor coordenador

### Servidor hotel:
* Consulta de hospedagem
* Compra de hospedagem
* Compra de hospedagem dentro de uma transação

### Servidor companhia aérea:
* Consulta de passagem
* Compra de passagem
* Compra de passagem dentro de uma transação

### Servidor coordenador:
* Consulta de hospedagem
* Consulta de passagem
* Compra de hospedagem
* Compra de passagem
* Compra de pacotes

### Compra de pacotes com transação:
* Two-Phase Commit
* Coordenador + participantes (servidor hotel e servidor companhia aérea)

### Propriedades ACID:
* Atomicidade: Ou faz a transação inteira ou não faz nada
* Consistência: Transações devem levar o sistema de um estado consistente para um estado consistente
* Isolamento: Transações devem ocorrer de maneira isolada, sem interferir uma na outra
* Durabilidade: Os dados devem ser persistentes (escrever em arquivo e fazer log das transações, restaurar estado após um crash)

### Outros requisitos
* Evitar escritas prematuras
* Evitar leituras sujas
* Evitar cancelamentos em cascata
* Evitar deadlock

* Registrar log dos estados das transações nos três servidores

* Em caso de falha, recuperar todos os dados após ser restabelecido:
    * Estado das transações
    * Dados intermediários de uma transação incompleta
    * Dados finais de transações efetivadas

* Os participantes devem possuir dois arquivos:
    * Final, visível por todos os processos
    * Intermediário, visível apenas pelo processo corrente
    * **Não usar banco de dados**

* Utilizar mecanismo de controle de acesso concorrente aos objetos compartilhados
