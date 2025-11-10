-- Inserção dos clientes
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9001', 'Huguinho Pato', '51985744566', 'Rua das Flores, 100', 'huguinho.pato@email.com');
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9002', 'Luizinho Pato', '5199172079', 'Av. Central, 200', 'zezinho.pato@email.com');

-- Inserção dos ingredientes
INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza');
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate');
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela');
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto');
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa');
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)');
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona');
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano');
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola');

-- Inserção dos itens de estoque
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (1, 30, 1);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (2, 30, 2);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (3, 30, 3);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (4, 30, 4);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (5, 30, 5);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (6, 30, 6);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (7, 30, 7);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (8, 30, 8);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (9, 30, 9);

-- Inserção das receitas 
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa');
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto');
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita');

-- Associação dos ingredientes à receita Pizza calabresa
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 5); -- Porcao de calabresa
-- Associação dos ingredientes à receita Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 4); -- Porcao de presunto
-- Associação dos ingredientes à receita Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 8); -- Porcao de cebola

-- insercao dos produtos
INSERT INTO produtos (id,descricao,preco) VALUES (1,'Pizza calabresa',5500);
INSERT INTO produtos (id,descricao,preco) VALUES (2,'Pizza queijo e presunto',6000);
INSERT INTO produtos (id,descricao,preco) VALUES (3,'Pizza margherita',4000);

-- Associação dos produtos com as receitas
INSERT INTO produto_receita (produto_id,receita_id) VALUES(1,1);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(2,2);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(3,3);

-- Insercao dos cardapios
INSERT INTO cardapios (id,titulo) VALUES(1,'Cardapio de Agosto');
INSERT INTO cardapios (id,titulo) VALUES(2,'Cardapio de Setembro');

-- Associação dos cardapios com os produtos
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,2);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,3);

INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,3);

-- Inserção de um pedido de exemplo (Aprovado, mas não pago)
-- Cliente: Huguinho Pato (cpf: '9001')
-- Itens: 1x Pizza Calabresa (R$ 55,00) + 2x Pizza Margherita (R$ 40,00 cada = R$ 80,00)
-- Valor total dos itens: R$ 135,00
-- Impostos (10%): R$ 13,50
-- Desconto: R$ 0,00
-- Valor cobrado: R$ 148,50
INSERT INTO pedidos (id, cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) 
    VALUES (1, '9001', 'APROVADO', NULL, 135.00, 13.50, 0.00, 148.50);
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (1, 1, 1); -- 1x Pizza calabresa
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (1, 3, 2); -- 2x Pizza margherita

-- PEDIDO 2: Pedido Cancelado (Testar UC4)
-- Cliente: Luizinho Pato (cpf: '9002')
-- Itens: 1x Pizza queijo e presunto (R$ 60,00)
INSERT INTO pedidos (id, cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) 
    VALUES (2, '9002', 'CANCELADO', '2025-10-10 14:30:00', 60.00, 6.00, 0.00, 66.00);
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (2, 2, 1);

-- PEDIDO 3: Pedido Entregue (Testar UC6 - Listar Entregues)
-- Cliente: Huguinho Pato (cpf: '9001')
-- Itens: 1x Pizza margherita (R$ 40,00)
-- A data de pagamento/entrega deve ser do passado para ter resultados no relatório.
INSERT INTO pedidos (id, cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) 
    VALUES (3, '9001', 'ENTREGUE', '2025-10-10 14:30:00', 40.00, 4.00, 0.00, 44.00);
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (3, 3, 1);

-- PEDIDO 4: Pedido para Teste de Pagar (UC5)
-- Cliente: Luizinho Pato (cpf: '9002')
-- Itens: 2x Pizza calabresa (R$ 55,00 cada = R$ 110,00)
-- Status inicial é APROVADO, pronto para ser pago.
INSERT INTO pedidos (id, cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) 
    VALUES (4, '9002', 'APROVADO', '2025-10-12 14:30:00', 110.00, 11.00, 0.00, 121.00);
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (4, 1, 2);

-- PEDIDO 5: Pedido Entregue (Testar UC6 - Listar Entregues)
-- Cliente: Luizinho Pato (cpf: '9002')
-- Itens: 3x Pizza margherita (R$ 40,00 cada = R$ 120,00)
-- A data de pagamento/entrega deve ser do passado para ter resultados no relatório.
INSERT INTO pedidos (id, cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) 
    VALUES (5, '9002', 'ENTREGUE', '2025-10-14 21:30:00', 120.00, 12.00, 0.00, 132.00);
-- CORRIGIDO: O item de pedido deve ter pedido_id=5 e a quantidade deve ser 3.
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (5, 3, 3);

-- Usuario Master 
-- senha codificada = senhamaster123
INSERT INTO usuarios (email, senha, tipo) 
VALUES ('master@pizzaria.com', '$2b$10$r.YShQoQrDK9BDJzkoCcjO5jYvn/9OcN1BHM3OHYifGNQde7nkIq6', 'MASTER');
