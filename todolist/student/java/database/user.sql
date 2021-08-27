-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER todolist_owner
WITH PASSWORD 'todolist';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO todolist_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO todolist_owner;

CREATE USER todolist_appuser
WITH PASSWORD 'listitems';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO todolist_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO todolist_appuser;
