# Desafio ID Brasil - aplicação ID Market
Sistema de marketplace para gestão de produtos e pedidos.

---

## Arquitetura
Por falta de conhecimento na arquitetura de micro serviços, foi optado pela arquitetura monolítica organizada nas seguintes camadas:

- controllers: contém endpoints REST e tratamento de erros dos endpoints
- services: contém as regras de negócio
- repositories: interfaces de acesso a dados
- entities: entidades do negócio
- dto: objetos DTO para transferência de dados de cada entidade
- mappers: conversores de entitdade e de DTO
- validations: validações personalizadas
- exceptions: contém classes de exceção e mensagens de erro

---

## Coleção Postman
A coleção de requisições do Postman para testes manuais pode ser encontrada na pasta [docs](docs) e contém requests para todos os endpoints criados.

---

## Como executar
Primeiramente faça um clone do repositório:

    git clone https://github.com/Hilan-CP/desafio-backend-idbrasil.git


### Execução local
Compile o projeto:

    .\mvnw.cmd clean package

Depois execute:

    java -jar .\target\idmarket-0.0.1-SNAPSHOT.jar

Executar testes automatizados (unitário e integração):

    .\mvnw.cmd test


### Execução com Docker
Execute o comando:

    docker-compose up -d
