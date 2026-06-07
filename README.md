# Raízes do Nordeste — API Back-End

API REST desenvolvida em Java com Spring Boot para gerenciamento centralizado de pedidos, pagamentos, estoque e fidelidade da rede de franquias **Raízes do Nordeste**.

---

## Requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java (JDK) | 17 |
| Maven | 3.9+ |
| MySQL | 8.0+ |

---

## Configuração de ambiente

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE raizes_nordeste;
```

As credenciais padrão usadas pela aplicação são `root` / `root_password`. Para alterar, edite o arquivo:

```
src/main/resources/application.properties
```

```properties
spring.datasource.jdbc-url=jdbc:mysql://localhost:3306/raizes_nordeste?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=root
spring.datasource.password=root_password
```

> O parâmetro `createDatabaseIfNotExist=true` cria o banco automaticamente se ele não existir.

---

## Instalação de dependências

```bash
mvn clean install -DskipTests
```

---

## Banco de dados e migrations

O projeto utiliza JDBC puro (sem ORM). As tabelas devem ser criadas manualmente antes de iniciar a API.

Execute o script de criação das tabelas no MySQL:

```sql
-- Usuários / Funcionários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo ENUM('GERENTE','ATENDENTE','COZINHA') NOT NULL,
    unidade_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Unidades da rede
CREATE TABLE unidades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE
);

-- Produtos do cardápio
CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE
);

-- Estoque por unidade
CREATE TABLE estoque (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unidade_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    FOREIGN KEY (unidade_id) REFERENCES unidades(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Pedidos
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    unidade_id INT NOT NULL,
    canal_pedido ENUM('TOTEM','BALCAO','APP','PICKUP','SITE') NOT NULL,
    status ENUM('AGUARDANDO_PAGAMENTO','EM_PREPARACAO','EM_ENTREGA','ENTREGUE','CANCELADO') NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
    valor_total DECIMAL(10,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (unidade_id) REFERENCES unidades(id)
);

-- Itens do pedido
CREATE TABLE itens_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Pagamentos
CREATE TABLE pagamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    forma_pagamento ENUM('PIX','DINHEIRO','CREDITO','DEBITO') NOT NULL,
    status ENUM('PENDENTE','APROVADO','RECUSADO','CANCELADO') NOT NULL DEFAULT 'PENDENTE',
    valor DECIMAL(10,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);

-- Fidelidade (pontos)
CREATE TABLE fidelidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    pontos INT NOT NULL DEFAULT 0,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Seed de dados para teste:**

```sql
-- Unidade
INSERT INTO unidades (nome, cidade) VALUES ('Unidade Centro', 'Fortaleza');

-- Usuários (senhas em BCrypt — "Senha@123")
INSERT INTO usuarios (nome, email, senha, cargo, unidade_id) VALUES
('Carlos Gerente', 'gerente@raizes.com', '$2a$10$944UVM9lZNIspIucUF3XmOJ.0To/fHVqY/NdF/GD2rHuwGCIu.zYq', 'GERENTE', 1),
('Ana Atendente', 'atendente@raizes.com', '$2a$10$944UVM9lZNIspIucUF3XmOJ.0To/fHVqY/NdF/GD2rHuwGCIu.zYq', 'ATENDENTE', 1),
('João Cozinha', 'cozinha@raizes.com', '$2a$10$944UVM9lZNIspIucUF3XmOJ.0To/fHVqY/NdF/GD2rHuwGCIu.zYq', 'COZINHA', 1);

-- Produtos
INSERT INTO produtos (nome, descricao, preco, categoria) VALUES
('Baião de Dois', 'Feijão com arroz e carne seca', 32.90, 'Prato Principal'),
('Carne de Sol', 'Carne de sol com macaxeira frita', 45.90, 'Prato Principal'),
('Tapioca Recheada', 'Tapioca com queijo coalho e carne', 23.90, 'Lanche');

-- Estoque (produto_id 1, 2, 3 na unidade 1)
INSERT INTO estoque (unidade_id, produto_id, quantidade) VALUES
(1, 1, 50),
(1, 2, 30),
(1, 3, 100);
```

---

## Como iniciar a API

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## Documentação Swagger / OpenAPI

Acesse após iniciar a API:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **JSON OpenAPI:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Coleção Postman

O arquivo da coleção está na raiz do repositório:

```
raizes-api.postman_collection.json
```

**Como importar:**
1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `raizes-api.postman_collection.json`
4. A variável `baseUrl` já está configurada como `http://localhost:8080`

**Ordem de execução recomendada para os testes:**

| # | Pasta | Requisição |
|---|---|---|
| 1 | Auth | T01 - Login válido (obtém o cookie JWT) |
| 2 | Pedidos | T03 - Criar pedido com estoque suficiente |
| 3 | Pagamentos | T08 - Pagamento mock aprovado |
| 4 | Pedidos | T07 - Atualizar status do pedido |
| 5 | Pagamentos | T09 - Pagamento mock recusado |
| 6 | Erros | T10 - Requisição sem cookie JWT (401) |
| 7 | Erros | T11 - Acesso com cargo sem permissão (403) |
| 8 | Auth | T02 - Login com credenciais inválidas (401) |
| 9 | Pedidos | T04 - Campo canalPedido ausente (400) |
| 10 | Pedidos | T05 - Produto inexistente (404) |
| 11 | Pedidos | T06 - Estoque insuficiente (409) |
| 12 | Erros | T12 - Listar sem filtros (400) |

> **Autenticação:** o JWT é retornado como cookie `httpOnly` no login. O Postman gerencia o cookie automaticamente após o T01.

---

## Repositório

[https://github.com/NacDaniel/JAVA-PROJETO-MULTIDISCIPLINAR-UNINTER](https://github.com/NacDaniel/JAVA-PROJETO-MULTIDISCIPLINAR-UNINTER)

---

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security + JWT (cookie httpOnly)
- JDBC puro via `NamedParameterJdbcTemplate`
- MySQL 8
- SpringDoc OpenAPI (Swagger)
- BCrypt para hash de senhas
