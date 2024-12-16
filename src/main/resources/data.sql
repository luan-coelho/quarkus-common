CREATE SCHEMA IF NOT EXISTS audit;

-- Inserir módulo
INSERT INTO module (active, version, createdat, updatedat, id, name)
VALUES (true, 1, NOW(), NOW(), '123e4567-e89b-12d3-a456-426614174000', 'Gestão - Segurança');

-- Menu Item: Relatórios
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174001', 'puzzle', 'Módulos', '/modules');

-- Menu Item: Relatórios
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '08695e2e-1794-49b5-97d1-c75f02c6d42c', 'square-menu', 'Itens de Menu', '/menu-items');

-- Relacionar módulo com os itens de menu
INSERT INTO module_menu_item (module_id, menuitems_id)
VALUES ('123e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174001');

INSERT INTO module_menu_item (module_id, menuitems_id)
VALUES ('123e4567-e89b-12d3-a456-426614174000', '08695e2e-1794-49b5-97d1-c75f02c6d42c');

INSERT INTO "user" (active, version, createdat, updatedat, id, email, name, password)
VALUES (true, 1, NOW(), NOW(), '123e4567-e89b-12d3-a456-426614174001', 'user@gmail.com', 'User', '123');

INSERT INTO user_module (user_id, module_id)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000');