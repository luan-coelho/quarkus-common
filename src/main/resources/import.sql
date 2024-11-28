CREATE SCHEMA IF NOT EXISTS audit;

-- Novo módulo
INSERT INTO module (active, version, createdat, updatedat, id, name)
VALUES (true, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174000', 'Outro Módulo');

-- Inserir menu items
-- Menu Item: Relatórios
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174001', NULL, 'file-chart', 'Relatórios', '/reports');

-- Menu Item: Usuários
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 2, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174002', NULL, 'users', 'Usuários', '/users');

-- Menu Item: Administração
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 3, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174003', NULL, 'tools', 'Administração', NULL);

-- Sub Item: Permissões
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174004', '223e4567-e89b-12d3-a456-426614174003', 'key', 'Permissões', '/admin/permissions');

-- Sub Item: Logs
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 2, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174005', '223e4567-e89b-12d3-a456-426614174003', 'list', 'Logs', '/admin/logs');

-- Menu Item: Ajuda
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 4, 1, NOW(), NOW(), '223e4567-e89b-12d3-a456-426614174006', NULL, 'question-circle', 'Ajuda', '/help');

-- Relacionar módulo com os itens de menu
INSERT INTO module_menu_item (module_id, menuitems_id)
VALUES
    ('223e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174001'),
    ('223e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174002'),
    ('223e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174003'),
    ('223e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174006');

-- Novo módulo
INSERT INTO module (active, version, createdat, updatedat, id, name)
VALUES (true, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174000', 'Gestão de Projetos');

-- Inserir menu items
-- Menu Item: Projetos
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174001', NULL, 'folder', 'Projetos', '/projects');

-- Menu Item: Equipes
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 2, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174002', NULL, 'people', 'Equipes', '/teams');

-- Menu Item: Ferramentas
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 3, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174003', NULL, 'toolbox', 'Ferramentas', NULL);

-- Sub Item: Integrações
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 1, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174003', 'plug', 'Integrações', '/tools/integrations');

-- Sub Item: APIs
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 2, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174005', '323e4567-e89b-12d3-a456-426614174003', 'code', 'APIs', '/tools/apis');

-- Menu Item: Calendário
INSERT INTO menu_item (active, position, version, createdat, updatedat, id, parent_id, icon, label, route)
VALUES (true, 4, 1, NOW(), NOW(), '323e4567-e89b-12d3-a456-426614174006', NULL, 'calendar', 'Calendário', '/calendar');

-- Relacionar módulo com os itens de menu
INSERT INTO module_menu_item (module_id, menuitems_id)
VALUES
    ('323e4567-e89b-12d3-a456-426614174000', '323e4567-e89b-12d3-a456-426614174001'),
    ('323e4567-e89b-12d3-a456-426614174000', '323e4567-e89b-12d3-a456-426614174002'),
    ('323e4567-e89b-12d3-a456-426614174000', '323e4567-e89b-12d3-a456-426614174003'),
    ('323e4567-e89b-12d3-a456-426614174000', '323e4567-e89b-12d3-a456-426614174006');

