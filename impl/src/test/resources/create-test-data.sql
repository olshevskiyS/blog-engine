CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  is_moderator TINYINT NOT NULL,
  reg_time TIMESTAMP NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  code VARCHAR(255),
  photo TEXT,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS posts (
  id INT NOT NULL AUTO_INCREMENT,
  is_active TINYINT NOT NULL,
  moderation_status VARCHAR(255) NOT NULL,
  moderator_id INT,
  user_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  title VARCHAR(255) NOT NULL,
  text TEXT NOT NULL,
  view_count INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (moderator_id) REFERENCES users (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS post_votes (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  value TINYINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (post_id) REFERENCES posts (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS post_comments (
  id INT NOT NULL AUTO_INCREMENT,
  parent_id INT,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  text TEXT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (parent_id) REFERENCES post_comments (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (post_id) REFERENCES posts (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS tags (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS tag2post (
  post_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  FOREIGN KEY (post_id) REFERENCES posts (id),
  FOREIGN KEY (tag_id) REFERENCES tags (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS captcha_codes (
  id INT NOT NULL AUTO_INCREMENT,
  time TIMESTAMP NOT NULL,
  code TINYTEXT NOT NULL,
  secret_code TINYTEXT NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS global_settings (
  id INT NOT NULL AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO users (email, is_moderator, name, password, reg_time, photo) VALUES
('user01@email.com', '1', 'пользователь01', '$2y$10$bRYprOEmBFBJatbnVj0bZOdXu//mUyypr.6MS28.aO8KGpun41QSS', '2021-12-17 08:00:00.00000', null),
('user02@email.com', '0', 'пользователь02', '$2y$10$F.NBILvoBhmK98iZdxD5be4wjt6KlLr3g4mu1c0PW2cdKrw7TnP7S', '2021-12-18 15:23:33.00000', 'https://res.cloudinary.com/dhtldn8id/image/upload/v1677875360/upload/1677875359-image-id1.jpg'),
('user03@email.com', '0', 'пользователь03', '$2y$10$Avc0qyiHCHNY7BKpCBa5qeW/NdIrMlUjJqdw0Hj8IcZ/VXFhvdGjK', '2021-12-19 21:20:45.00000', null);

INSERT INTO posts (is_active, moderation_status, moderator_id, text, time, title, user_id, view_count) VALUES
('1', 'ACCEPTED', '1', '<p>Добро пожаловать в мир гальваники! Здесь вы сможете найти своих единомышленников и большое количество интересной информации. Присоединяйтесь к нам и делитесь своей информацией.</p>', '2021-12-18 09:12:50.00000', 'Приветствие', '1', '4'),
('1', 'ACCEPTED', '1', '<p>Правила пользования блогом: 1. Приятный дизайн блога. 2. Минимум рекламы. 3. Блог это прежде всего дневник. Рассказывать о себе. 4. Узнали что-то новое-написали пост. 5. Работа с комментариями. 6. Блог должен быть уникальный во всем.</p>', '2021-12-18 09:28:00.00000', 'Правила', '1', '6'),
('1', 'ACCEPTED', '1', '<p>Цинкование – покрытие металла слоем цинка для защиты от коррозии. Подходит для ровных или с небольшим изгибом поверхностей, не подверженных механическим воздействиям. Толщина цинкового слоя зависит от температуры и продолжительности процесса цинкования.</p>', '2021-12-18 19:02:34.00000', 'Цинкование', '2', '2'),
('1', 'NEW', '1', '<p>Никелирование – обработка поверхности изделий путём нанесения на них никелевого покрытия. Толщина наносимого покрытия обычно составляет от 1 до 50 мкм. Как правило, никелированием обрабатывают металлические изделия, изготовленные из стали либо других металлов и сплавов.</p>', '2021-12-19 05:45:10.00000', 'Никелирование', '2', '0'),
('1', 'ACCEPTED', '1', '<p>Хромирование занимает особое место среди гальванических покрытий и находит применение во многих областях. К достоинствам хромирования относят высокую твердость покрытия, стойкость хромированных деталей к коррозии и воздействию агрессивной среды, жаростойкость, а также красивый внешний вид.</p>', '2021-12-19 23:18:07.00000', 'Хромирование', '3', '1'),
('1', 'DECLINED', '1', '<p>Как вырастить кактус. Выращивание кактуса из семян необходимо для того, чтобы обновить коллекцию, а также омолодить имеющиеся экземпляры. Это связано с тем, что по прошествии некоторого времени порода начнет вырождаться.</p>', '2021-12-20 11:48:27.00000', 'Кактусы', '3', '0'),
('1', 'ACCEPTED', '1', '<p>Детали больших размеров находятся в объемных ваннах в подвешенном состоянии. На более мелкие изделия гальваническое покрытие наносится в барабанных емкостях, где отрицательный заряд подается на барабан, который вращается в электролите.</p>', '2022-01-17 22:08:15.00000', 'Размер изделий', '2', '1'),
('0', 'NEW', null, '<p>Процесс обезжиривания. На любом изделии присутствует жировая пленка, мешающая качественному нанесению покрытия. Пленка удаляется моющим средством.</p>', '2022-01-24 11:28:27.00000', 'Обезжиривание', '3', '0');

INSERT INTO post_votes (post_id, time, user_id, value) VALUES
('1', '2021-12-18 16:10:35.00000', '2', '1'),
('1', '2021-12-19 21:37:14.00000', '3', '1'),
('3', '2021-12-20 01:03:20.00000', '3', '1'),
('5', '2021-12-20 13:20:47.00000', '2', '-1');

INSERT INTO post_comments (parent_id, post_id, text, time, user_id) VALUES
(null, '3', 'Интересно', '2021-12-18 20:15:57.00000', '1'),
(null, '3', 'Класс', '2021-12-20 01:06:16.00000', '3'),
(null, '5', 'Отлично', '2021-12-20 07:31:13.00000', '1'),
(null, '5', 'Устарело', '2021-12-20 13:22:07.00000', '2');

INSERT INTO tags (name) VALUES
('хромирование'),
('цинкование'),
('коррозия'),
('правила');

INSERT INTO tag2post (post_id, tag_id) VALUES
('2', '4'),
('3', '2'),
('3', '3'),
('5', '1'),
('5', '3');

INSERT INTO captcha_codes (time, code, secret_code) VALUE ('2022-01-18 16:10:35.00000', 'uyeq', 'dXllcQ==');

INSERT INTO global_settings (code, name, value) VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'YES'),
('POST_PREMODERATION', 'Премодерация постов', 'YES'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES')