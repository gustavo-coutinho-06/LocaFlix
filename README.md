# LocaFlix

Projeto de estudo de **Java 25** com **Spring Boot**. O LocaFlix é uma locadora de filmes: o usuário aluga e devolve títulos por uma interface em **React**, e a API no backend persiste os dados em **PostgreSQL**.

O banco sobe com **Docker Compose**, para o ambiente local ficar próximo de um cenário real sem instalar o PostgreSQL direto na máquina.

## O que dá para fazer

- Alugar um filme
- Devolver um filme alugado

Essas ações acontecem no front-end em React, que conversa com a API Spring Boot.

## Tecnologias

| Parte | Tecnologia |
| --- | --- |
| Backend | Java 25 e Spring Boot |
| Frontend | React |
| Banco de dados | PostgreSQL |
| Ambiente do banco | Docker Compose |

## Estrutura

- `BackEnd/LocaFlix` — API em Spring Boot
- `FrontEnd` — interface em React
