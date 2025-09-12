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

## Endpoints

- GET /products/{id} → busca um produto por id e retorna o produto
  - id: número inteiro (obrigatório)


- GET /products?sku=&nome=&ativo= → busca produtos pelos parâmetro passados e retorna resultados paginados
  - sku: string de código do produto (opcional)
  - nome: nome parcial do produto (opcional)
  - ativo: true para produtos ativos, false para produtos inativos (opcional)


- POST /products → salva um produto e retorna o produto salvo
  - enviar JSON no corpo da requisição
  - ```
    {
    "sku":"string obrigatório",
    "nome":"string obrigatório",
    "descricao":"string obrigatório",
    "preco": número decimal obrigatório,
    "estoque": número inteiro obrigatório
    }
    ```

- PUT /products/{id} → atualiza um produto e retorna o produto salvo
  - id: número inteiro (obrigatório)
  - enviar JSON no corpo da requisição
  - ```
    {
    "sku":"string obrigatório",
    "nome":"string obrigatório",
    "descricao":"string obrigatório",
    "preco": número decimal maior que zero obrigatório,
    "estoque": número inteiro maior ou igual a zero obrigatório
    }
    ```

- PATCH /products/{id}/stock → atualiza o estoque do produto e retorna o produto atualizado
  - id: número inteiro (obrigatório)
  - enviar JSON no corpo da requisição
  - ```
    {
    "estoque": número inteiro maior ou igual a zero obrigatório
    }
    ```
    
- DELETE /products/{id} → marca um produto como inativo
  - id: número inteiro (obrigatório)


- GET /orders/{id} → busca um pedido por id e retorna o pedido
  - id: número inteiro (obrigatório)


- GET /orders?customerId=&status= → busca pedidos pelos parâmetros fornecidos e retorna resultados paginados
  - customerId: número inteiro (opcional)
  - status: CREATED, PAID, CANCELLED (opcional)


- POST /orders → salva um pedido e retorna o pedido salvo
  - enviar JSON no corpo da requisição
  - ```
    {
    "customerId": número inteiro obrigatório,
    "items":[
        {
            "productSku":"string obrigatório",
            "quantity": número inteiro maior que zero obrigatório
        },
        {
            "productSku":"A lista deve ter no mínimo 1 item",
            "quantity":1
        }
        ]
    }
    ```


- POST /orders/{id}/pay → altera o status do pedido para pago e retorna o pedido
  - id: número inteiro (obrigatório)


- POST /orders/{id}/cancel → altera o status do pedido para cancelado e retorna o pedido
  - id: número inteiro (obrigatório)


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
