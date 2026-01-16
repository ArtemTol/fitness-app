-- Создаем базы данных для каждого сервиса
CREATE DATABASE fitness_core;
CREATE DATABASE fitness_gamification;
CREATE DATABASE fitness_social;
CREATE DATABASE fitness_notification;
CREATE DATABASE fitness_health;
CREATE DATABASE fitness_photo;

-- Даем права пользователю
GRANT ALL PRIVILEGES ON DATABASE fitness_core TO fitness_user;
GRANT ALL PRIVILEGES ON DATABASE fitness_gamification TO fitness_user;
GRANT ALL PRIVILEGES ON DATABASE fitness_social TO fitness_user;
GRANT ALL PRIVILEGES ON DATABASE fitness_notification TO fitness_user;
GRANT ALL PRIVILEGES ON DATABASE fitness_health TO fitness_user;
GRANT ALL PRIVILEGES ON DATABASE fitness_photo TO fitness_user;