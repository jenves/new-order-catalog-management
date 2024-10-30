Tecnologias mínimas que devem ser utilizadas:
• Banco de dados PostgreSQL
• Java 8+
• Maven
• Spring
• JPA
• Bean Validation
• QueryDSL
• REST com JSON

Requisitos da prova:

• Deverá ser desenvolvido um cadastro (Create/Read/Update/Delete/List com paginação)
para as seguintes entidades: produto/serviço, pedido e itens de pedido.
• Deverá ser possível aplicar filtros na listagem

• As entidades deverão utilizar Bean Validation

• Deverá ser implementado um ControllerAdvice para customizar os HTTP Response das
requisições (mínimo BAD REQUEST)

• Todos as entidades deverão ter um ID único do tipo UUID gerado automaticamente
(Mudar para o UUIDV7)

• No cadastro de produto/serviço deverá ter uma indicação para diferenciar um produto de
um serviço

• Deverá ser possível aplicar um percentual de desconto no pedido, porém apenas para os
itens que sejam produto (não serviço); o desconto será sobre o valor total dos produtos

• Somente será possível aplicar desconto no pedido se ele estiver na situação Aberto
(Fechado bloqueia)

• Não deve ser possível excluir um produto/serviço se ele estiver associado a algum pedido

• Não deve ser possível adicionar um produto desativado em um pedido

Critérios de aceitação

• A prova deverá ser entregue completa (todos os itens resolvidos)

• Deverão ser criados testes automatizados

• O código não poderá ter erros de compilação

• Deverá haver uma documentação mínima de como executar o projeto e suas
funcionalidades
