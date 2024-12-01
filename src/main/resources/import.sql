CREATE SCHEMA IF NOT EXISTS audit;

-- Inserir módulo
INSERT INTO module (active, version, createdat, updatedat, id, name)
VALUES (true, 1, NOW(), NOW(), '123e4567-e89b-12d3-a456-426614174000', 'Gestão de Projetos');

-- Menu Item: Relatórios
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174001', 'puzzle', 'Módulos', '/modules');

-- Relacionar módulo com os itens de menu
INSERT INTO module_menu_item (module_id, menuitems_id)
VALUES ('123e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174001');
