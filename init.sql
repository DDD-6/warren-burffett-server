create user test with password '1234' superuser;
alter user test with superuser;
alter user test with createdb;
alter user test with createrole;
alter user test with bypassrls;
alter user test with replication;
create database wesave owner test;
grant all privileges on database wesave to test;