# 🛒 E-Commerce API - Spring Boot

Uma API RESTful completa e robusta para gerenciamento de e-commerce, construída com foco em **Clean Code**, **Design Patterns**, **Alta Coesão** e **Testes Unitários**. 

Este projeto não é apenas um CRUD simples; ele engloba regras de negócio complexas como controle transacional de estoque, orquestração de carrinho de compras, motor de descontos (cupons) e um sistema de auditoria assíncrona.

## 🚀 Tecnologias e Ferramentas

* **Java 17+**
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **Banco de Dados:** Oracle DB (preparado com tratamentos para funções de agregação e CLOBs)
* **Testes:** JUnit 5 e Mockito
* **Utilitários:** Lombok, Jackson (ObjectMapper), MapStruct (opcional)

## 🏗️ Arquitetura e Padrões Aplicados

* **Arquitetura em Camadas:** Separação clara entre Controllers, Services, Repositories e Models.
* **DTO Pattern (Data Transfer Object):** Isolamento total entre as entidades de banco de dados e os contratos da API usando `RequestDTO`, `ResponseDTO` e `SnapshotDTO` (para auditoria).
* **Tratamento Global de Exceções:** Implementação do `@RestControllerAdvice` padronizando as respostas de erro HTTP (400, 404) e blindando regras de negócio.
* **Processamento Assíncrono (`@Async`):** Motor de auditoria rodando em background (threads separadas) para não impactar a latência do usuário final.
* **Design Defensivo:** Proteção contra manipulação indevida de dados por referência de memória e Lazy Loading Proxies do Hibernate (`ByteBuddyInterceptor`).

---

## 📦 Módulos Principais (Features)

### 1. Catálogo e Estoque (Produtos e Categorias)
* Relacionamento de Produtos e Categorias.
* Controle rigoroso de estoque na criação e finalização de pedidos.
* Produtos só podem ser atualizados pelo proprio vendedor.

### 2. Carrinho de Compras
* Criação automática de carrinho `ATIVO` por usuário.
* Adição, remoção e atualização de itens com "Congelamento de Preço" (Snapshot), garantindo que alterações no catálogo não afetem itens já no carrinho.
* Cálculos matemáticos financeiros dinâmicos (Subtotal e Total Geral).

### 3. Checkout e Pedidos
* Orquestração transacional (`@Transactional`): Converte o carrinho ativo em um pedido finalizado.
* Baixa automática de estoque.
* Validação rigorosa de carrinho vazio.


### 4. Promoções (Cupons)
* Tipos de Desconto: `PERCENTUAL` e `FIXO`.
* Aplicabilidade: `CARRINHO_TOTAL`, `PRODUTO` específico ou `CATEGORIA`.
* Regras de Validação: Data de validade, limite máximo de usos pela loja e **uso único por usuário** (rastreamento de cupons queimados).
* Regra Financeira: O desconto nunca pode ultrapassar o valor do subtotal aplicável.

### 5. Prova Social (Avaliações / Reviews)
* Avaliação de 1 a 5 estrelas.
* **Validação de Compra:** O sistema cruza os dados para garantir que o usuário só possa avaliar produtos que realmente comprou e que foram entregues.
* Recálculo automático de Média de Avaliação (tratando retornos vazios do banco com `COALESCE`).

### 6. Auditoria (Auditoria / Audit Log)
* Rastreamento imutável (Append-only) de todas as alterações críticas (Criação, Alteração, Exclusão).
* Gravação de Snapshots em JSON (`jsonAntes` e `jsonDepois`) usando `ObjectMapper`.
* Processamento assíncrono para garantir altíssima performance nos endpoints principais.

---


## ⚙️ Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone [https://github.com/Rogwillians/jp-capacitacao-roger](https://github.com/Rogwillians/jp-capacitacao-roger)
