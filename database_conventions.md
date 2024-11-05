Para a estruturação de bancos de dados, algumas convenções são amplamente utilizadas para manter consistência e facilitar a manutenção. 

1. **Nomenclatura de Tabelas**:
    - Utilize **nomes no singular** para tabelas (`Usuario` ao invés de `Usuarios`), pois cada registro representa uma única entidade.
    - Prefixos geralmente não são recomendados para tabelas, a não ser em casos específicos de sistemas legados. Se usar, utilize algo curto e significativo (`inv_Produto` para tabelas relacionadas a inventário).
    - **Evite abreviações**, exceto quando amplamente compreendidas. Por exemplo, `Cliente` ao invés de `Cli`.

2. **Nomenclatura de Colunas**:
    - Use nomes descritivos e no singular (`nome`, `data_criacao`).
    - Colunas de chave estrangeira devem seguir o nome da tabela referenciada com `_id` (`usuario_id` para referência à tabela `Usuario`).
    - Utilize o padrão **snake_case** para separar palavras (`data_criacao` ao invés de `dataCriacao`).

3. **Nomenclatura de Chaves Primárias e Estrangeiras**:
    - Use `id` como coluna primária padrão.
    - Para chaves estrangeiras, o padrão é `<tabela_referenciada>_id` (ex.: `usuario_id`).
    - Índices podem seguir a convenção: `idx_<tabela>_<coluna>`.

4. **Nomeação de Índices e Constraints**:
    - Índices: `idx_<tabela>_<coluna>`.
    - Chave Primária: `pk_<tabela>`.
    - Chave Estrangeira: `fk_<tabela_origem>_<tabela_destino>`.
    - Restrições Unique: `uq_<tabela>_<coluna>`.

5. **Padrão de Data e Tempo**:
    - Use o tipo `timestamp` ou `datetime` para colunas de data e hora, e nomeie-as de forma descritiva (`data_criacao`, `data_atualizacao`).
    - Prefira usar **UTC** para dados globais, para facilitar a integração com outras aplicações.
