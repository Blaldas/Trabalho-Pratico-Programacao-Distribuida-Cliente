# Trabalho-Pratico-Programacao-Distribuida-Cliente
Programa de Cliente entregue no trabalho prático da cadeira de Programação Distribuida


# Equipa 

- [Marco Domingues](https://github.com/Blaldas)
- [Nuno Santos](https://github.com/MidgetSlayer)
- [Miguel Fazenda](https://github.com/D1scak3)


# Background

Este programa é o cliente pertencente à primeira meta do trabalho prático para a cadeira de Progaramação Distribuida.
O programa servidor com o qual deve fazer ligação pode ser encontrado [aqui](https://github.com/Blaldas/Trabalho-Pratico-Programacao-Distribuida-Servidor).


# Caracteristicas

Este cliente cria um pedido incial de coneção para com o servidor através de uma ligação UDP, sendo que, caso o servidor aceite a coneção, uma ligação TCP é establecida.
São utilizadas threads para que o programa consiga realizar as multiplas ações necessárias.


# Funcionalidades

- Fazer conecção UDP e TCP a um servidor;
- Registar um utilizador;
- Fazer login de um utilizador já registado;
- Mandar mensagens diretas para outro utilizador ligado ao mesmo servidor;
- Receber mensagens sincronamente;
- Listar mensagens trocadas com outro utilizador;
- Criar um canal onde multiplos utilizadores se podem conectar;
- Listar mensagens enviadas para um canal;
- Entre Outros...
