create schema if not exists petclinic_test;

grant all PRIVILEGES on schema petclinic_test to "user";

-- Понижаем уровень логгирования, чтобы видеть всё.
alter system set log_statement='all';

-- Создаём расширение для pg_stat_statements
alter system set shared_preload_libraries='pg_stat_statements';
CREATE EXTENSION pg_stat_statements;