INSERT INTO global_settings (code, name, value) VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'YES'),
('POST_PREMODERATION', 'Премодерация постов', 'YES'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES');

INSERT INTO users (code, email, is_moderator, name, password, reg_time) VALUES
('1111', 'user01@email.com', '1', 'пользователь01', '0101', '2021-12-17 08:00:00.00000'),
('2222', 'user02@email.com', '0', 'пользователь02', '0202', '2021-12-18 15:23:33.00000'),
('3333', 'user03@email.com', '0', 'пользователь03', '0303', '2021-12-19 21:20:45.00000');

INSERT INTO posts (is_active, moderation_status, moderator_id, text, time, title, user_id, view_count) VALUES
('1', 'ACCEPTED', '1', '<textarea>Добро пожаловать в мир гальваники! Здесь вы сможете найти своих единомышленников и большое количество интересной информации. Присоединяйтесь к нам и делитесь своей информацией.</textarea>', '2021-12-18 09:12:50.00000', 'Приветствие', '1', '4'),
('1', 'ACCEPTED', '1', '<textarea>Правила пользования блогом: 1. Приятный дизайн блога. 2. Минимум рекламы. 3. Блог это прежде всего дневник. Рассказывать о себе. 4. Узнали что-то новое-написали пост. 5. Работа с комментариями. 6. Блог должен быть уникальный во всем.</textarea>', '2021-12-18 09:28:00.00000', 'Правила', '1', '6'),
('1', 'ACCEPTED', '1', '<textarea>Цинкование – покрытие металла слоем цинка для защиты от коррозии. Подходит для ровных или с небольшим изгибом поверхностей, не подверженных механическим воздействиям. Толщина цинкового слоя зависит от температуры и продолжительности процесса цинкования.</textarea>', '2021-12-18 19:02:34.00000', 'Цинкование', '2', '2'),
('0', 'NEW', '1', '<textarea>Никелирование – обработка поверхности изделий путём нанесения на них никелевого покрытия. Толщина наносимого покрытия обычно составляет от 1 до 50 мкм. Как правило, никелированием обрабатывают металлические изделия, изготовленные из стали либо других металлов и сплавов.</textarea>', '2021-12-19 05:45:10.00000', 'Никелирование', '2', '0'),
('1', 'ACCEPTED', '1', '<textarea>Хромирование занимает особое место среди гальванических покрытий и находит применение во многих областях. К достоинствам хромирования относят высокую твердость покрытия, стойкость хромированных деталей к коррозии и воздействию агрессивной среды, жаростойкость, а также красивый внешний вид.</textarea>', '2021-12-19 23:18:07.00000', 'Хромирование', '3', '1'),
('0', 'DECLINED', '1', '<textarea>Как вырастить кактус. Выращивание кактуса из семян необходимо для того, чтобы обновить коллекцию, а также омолодить имеющиеся экземпляры. Это связано с тем, что по прошествии некоторого времени порода начнет вырождаться.</textarea>', '2021-12-20 11:48:27.00000', 'Кактусы', '3', '0');

INSERT INTO post_votes (post_id, time, user_id, value) VALUES
('1', '2021-12-18 16:10:35', '2', '1'),
('1', '2021-12-19 21:37:14', '3', '1'),
('3', '2021-12-20 01:03:20', '3', '1'),
('5', '2021-12-20 13:20:47', '2', '-1');

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