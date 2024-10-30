# order-catalog-management

Sistema abrangente projetado para gerenciar ordens, produtos/serviços e itens de ordem. Ele oferece uma API robusta para criar, ler, atualizar, excluir e listar essas entidades com suporte à paginação.

![tecnologia Java](https://img.shields.io/badge/tecnologia-Java-purple.svg)
![tecnologia Maven](https://img.shields.io/badge/tecnologia-Maven-blue.svg)
![tecnologia Spring](https://img.shields.io/badge/tecnologia-Spring-green)

## Pré-requisitos

- [**Java 17**](https://www.oracle.com/java/technologies/downloads/#java21)
- [**Maven**](https://maven.apache.org/download.cgi)
- [**Spring Boot 3**](https://spring.io/projects/spring-boot)
- [**Docker**](https://www.docker.com/products/docker-desktop/) - Para gerenciamento de banco de dados

## Estrutura do Projeto

O projeto segue uma estrutura padrão do Maven.

Componentes principais incluem:
- Controladores para manipulação de solicitações HTTP
- Serviços para lógica de negócios
- Repositórios para interações com banco de dados
- Modelos representando entidades de domínio
- Classes de configuração para setup da aplicação

## Arquitetura

O sistema emprega uma arquitetura em camadas:

1. Camada de Apresentação: Endpoints API RESTful
2. Camada de Aplicação: Classes de serviço implementando lógica de negócios
3. Modelo de Domínio: Classes de entidade representando produtos, serviços, ordens e itens de ordem
4. Camada de Acesso a Dados: Interfaces de repositório para operações de banco de dados
5. Camada de Infraestrutura: Configuração e conexão de banco de dados

## Execução

### Instalando dependências
```shell
./mvnw clean install
```
### Rodando os testes
```shell
./mvnw clean test
```

### Executando a aplicação
1. Subir o banco de dados PostgreSQL com Docker:
```shell
docker compose -f sandbox/docker-compose.yml up -d
```
2. Executar o comando:
```shell
./mvnw spring-boot:run
```

## Documentação da API <br>

- UI: http://localhost:8080/order-catalog-management/swagger-ui/index.html
- OpenAPI 3.0: http://localhost:8080/order-catalog-management/v3/api-docs

## Estilo de código

Siga o [guia de estilo de código Java do Google](https://google.github.io/styleguide/javaguide.html).

## Funcionalidades

1. Cadastro de produtos/serviços com indicação de tipo
2. Gerenciamento de pedidos (Create/Read/Update/Delete/List com paginação)
3. Adição/remoção de itens de pedido
4. Aplicação de filtros na listagem de entidades
5. Validação de dados usando Bean Validation
6. Implementação de ControllerAdvice para customização de respostas HTTP
7. Aplicação de desconto percentual em pedidos abertos (apenas para produtos)
8. Restrições de exclusão de produtos associados a pedidos
9. Bloqueio de adição de produtos desativados em pedidos

## Testes Unitários

O projeto inclui testes unitários para garantir a qualidade e confiabilidade do sistema.

## Configuração do Banco de Dados

Para configurar o banco de dados PostgreSQL, edite o arquivo `application.yml` no diretório `src/main/resources`.

## Instruções de Uso

1. Clone o repositório
2. Configure o banco de dados PostgreSQL
3. Execute o comando `mvn spring-boot:run` para iniciar a aplicação
4. Acesse a documentação da API via Swagger UI ou OpenAPI 3.0 para detalhes sobre os endpoints disponíveis
 
Este sistema atende aos requisitos especificados no documento PROBLEM.md, fornecendo uma solução completa para gerenciamento de catálogo de ordens.