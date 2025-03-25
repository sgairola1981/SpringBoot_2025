CREATE DATABASE IF NOT EXISTS gairola_db;
GRANT ALL PRIVILEGES ON gairola_db.* TO 'gairola'@'%';
ALTER USER 'gairola'@'%' IDENTIFIED WITH mysql_native_password BY 'gairola';
FLUSH PRIVILEGES;
