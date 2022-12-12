INSERT INTO global_settings (code, name, value) VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'YES'),
('POST_PREMODERATION', 'Премодерация постов', 'YES'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES');

INSERT INTO users (email, is_moderator, name, password, reg_time) VALUES
('user01@email.com', '1', 'пользователь01', '$2y$10$bRYprOEmBFBJatbnVj0bZOdXu//mUyypr.6MS28.aO8KGpun41QSS', '2021-12-17 08:00:00.00000'),
('user02@email.com', '0', 'пользователь02', '$2y$10$F.NBILvoBhmK98iZdxD5be4wjt6KlLr3g4mu1c0PW2cdKrw7TnP7S', '2021-12-18 15:23:33.00000'),
('user03@email.com', '0', 'пользователь03', '$2y$10$Avc0qyiHCHNY7BKpCBa5qeW/NdIrMlUjJqdw0Hj8IcZ/VXFhvdGjK', '2021-12-19 21:20:45.00000'),
('user04@email.com', '0', 'пользователь04', '$2y$10$SOMUpCd1WshciO2ASfTnlez22b1i2mGvDSx9SuJCnevbUrI5E9GbC', '2021-12-20 19:10:34.00000'),
('user05@email.com', '0', 'пользователь05', '$2y$10$EXvIGjKes8XkRFgbdoQEWuLvcWUIPuETpCTt.Fbb80HziPWZEFvG.', '2022-01-10 09:30:05.00000');

INSERT INTO posts (is_active, moderation_status, moderator_id, text, time, title, user_id, view_count) VALUES
('1', 'ACCEPTED', '1', '<p>Добро пожаловать в мир гальваники! Здесь вы сможете найти своих единомышленников и большое количество интересной информации. Присоединяйтесь к нам и делитесь своей информацией.</p>', '2021-12-18 09:12:50.00000', 'Приветствие', '1', '4'),
('1', 'ACCEPTED', '1', '<p>Правила пользования блогом: 1. Приятный дизайн блога. 2. Минимум рекламы. 3. Блог это прежде всего дневник. Рассказывать о себе. 4. Узнали что-то новое-написали пост. 5. Работа с комментариями. 6. Блог должен быть уникальный во всем.</p>', '2021-12-18 09:28:00.00000', 'Правила', '1', '6'),
('1', 'ACCEPTED', '1', '<p>Цинкование – покрытие металла слоем цинка для защиты от коррозии. Подходит для ровных или с небольшим изгибом поверхностей, не подверженных механическим воздействиям. Толщина цинкового слоя зависит от температуры и продолжительности процесса цинкования.</p>', '2021-12-18 19:02:34.00000', 'Цинкование', '2', '2'),
('1', 'NEW', '1', '<p>Никелирование – обработка поверхности изделий путём нанесения на них никелевого покрытия. Толщина наносимого покрытия обычно составляет от 1 до 50 мкм. Как правило, никелированием обрабатывают металлические изделия, изготовленные из стали либо других металлов и сплавов.</p>', '2021-12-19 05:45:10.00000', 'Никелирование', '2', '0'),
('1', 'ACCEPTED', '1', '<p>Хромирование занимает особое место среди гальванических покрытий и находит применение во многих областях. К достоинствам хромирования относят высокую твердость покрытия, стойкость хромированных деталей к коррозии и воздействию агрессивной среды, жаростойкость, а также красивый внешний вид.</p>', '2021-12-19 23:18:07.00000', 'Хромирование', '3', '1'),
('1', 'DECLINED', '1', '<p>Как вырастить кактус. Выращивание кактуса из семян необходимо для того, чтобы обновить коллекцию, а также омолодить имеющиеся экземпляры. Это связано с тем, что по прошествии некоторого времени порода начнет вырождаться.</p>', '2021-12-20 11:48:27.00000', 'Кактусы', '3', '0'),
('1', 'ACCEPTED', '1', '<p>Травление - это удаление оксидов на изделии (ржавчина, окалина и т.д.). Для травления используют кислоты (соляную, серную) или концентрированную щелочь. Процесс необходим для получения качественного сцепления покрытия с изделием.</p>', '2021-12-20 21:10:10.00000', 'Травление', '4', '2'),
('1', 'ACCEPTED', '1', '<p>Процесс обезжиривания. На любом изделии присутствует жировая пленка, мешающая качественному нанесению покрытия. Пленка удаляется моющим средством.</p>', '2021-12-20 21:41:11.00000', 'Обезжиривание', '3', '1'),
('1', 'ACCEPTED', '1', '<p>Соответствие техническим требованиям заказчика проверяет специалист Отдела Технического Контроля (ОТК). Измеряет толщину, адгезию (прочность сцепления), блеск, паяемость и кучу других параметров.</p>', '2021-12-24 09:11:57.00000', 'Контроль', '3', '2'),
('1', 'ACCEPTED', '1', '<p>Гальванику металла на поверхности используют для придания им свойств конкретного материала (серебро, золото, никель и т.д.). Либо если из этого материала невозможно изготовить предмет, а также если цена будет неоправданно высока.</p>', '2021-12-25 14:22:37.00000', 'Назначение гальванического метода', '4', '2'),
('1', 'ACCEPTED', '1', '<p>В ходе осаждения металлов важна температура электролита. Иногда используется дополнительное нагревательное устройство, которое погружается в гальваническую ванну или находится вне ее.</p>', '2021-12-26 08:50:21.00000', 'Температура электролита', '4', '3'),
('1', 'ACCEPTED', '1', '<p>Промывку осуществляют проточной горячей, а затем холодной водой, а после дополнительно промывают в дистиллированной воде. Последняя нужна чтобы не позволить проточной воде (и содержимся в ней вредным примесям) попасть в электролиты.</p>', '2021-12-27 10:36:48.00000', 'Промывка', '3', '2'),
('1', 'ACCEPTED', '1', '<p>В гальванике широко распространен метод гальванопластики. При этом изделие, погружаемое в гальваническую ванну, выступает в роли негатива, то есть покрытие растет не на рабочей стороне изделия а на задней, обратной стороне. На форму из непроводящего материалы осаждается слой металла, чаще всего это медь.</p>', '2022-01-10 13:08:18.00000', 'Гальванопластика', '5', '3'),
('1', 'NEW', '1', '<p>Покрытия-смазки - при их нанесении не столько учитываются химические характеристки самого металла, сколько необходимо обеспечить, например, плотную притирку деталей, но по какой-то причине нельзя использовать смазочные материалы. Это оловянные, свинцовые, индиевые покрытия.</p>', '2022-01-16 18:38:25.00000', 'Покрытия-смазки', '3', '0'),
('1', 'ACCEPTED', '1', '<p>Детали больших размеров находятся в объемных ваннах в подвешенном состоянии. На более мелкие изделия гальваническое покрытие наносится в барабанных емкостях, где отрицательный заряд подается на барабан, который вращается в электролите.</p>', '2022-01-17 22:08:15.00000', 'Размер изделий', '5', '1'),
('1', 'ACCEPTED', '1', '<p>Большое значение имеет плотность тока, который проходит через электролит. Она влияет на структуру формируемого осадка. Данная величина измеряется отношением силы тока к единице поверхности обрабатываемой детали.</p>', '2022-01-20 10:15:27.00000', 'Плотность тока', '5', '5'),
('1', 'ACCEPTED', '1', '<p>Слой родия увеличивает сопротивляемость деталей воздействию химически агрессивных сред, а также придает им дополнительную механическую стойкость. Родирование предотвращает окисление, потускнение изделий из серебра.</p>', '2022-01-21 15:00:27.00000', 'Родирование', '4', '3'),
('1', 'ACCEPTED', '1', '<p>В качестве покрытия используется медный купорос. Такая обработка способствует повышению прочности металлических изделий и повышению их токопроводящих свойств. Металлы с медным покрытием используются для производства электропроводников.</p>', '2022-02-05 11:50:20.00000', 'Меднение', '5', '4');

INSERT INTO post_votes (post_id, time, user_id, value) VALUES
('1', '2021-12-18 16:10:35', '2', '1'),
('1', '2021-12-19 21:37:14', '3', '1'),
('3', '2021-12-20 01:03:20', '3', '1'),
('5', '2021-12-20 13:20:47', '2', '-1'),
('7', '2021-12-20 21:25:35', '3', '1'),
('10', '2021-12-26 13:14:04', '2', '1'),
('12', '2021-12-27 18:04:58', '4', '-1'),
('16', '2021-01-20 15:29:18', '2', '1'),
('16', '2021-01-20 17:01:50', '4', '1'),
('16', '2021-01-20 21:15:02', '3', '1');

INSERT INTO post_comments (parent_id, post_id, text, time, user_id) VALUES
(null, '3', 'Интересно', '2021-12-18 20:15:57.00000', '1'),
(null, '3', 'Класс', '2021-12-20 01:06:16.00000', '3'),
(null, '5', 'Отлично', '2021-12-20 07:31:13.00000', '1'),
(null, '5', 'Устарело', '2021-12-20 13:22:07.00000', '2'),
(null, '10', 'О, то, что надо!', '2021-12-26 13:15:57.00000', '2'),
(null, '15', 'Подробнее, пожалуйста', '2021-01-18 05:36:28.00000', '3');

INSERT INTO tags (name) VALUES
('хромирование'),
('цинкование'),
('коррозия'),
('правила'),
('технология');

INSERT INTO tag2post (post_id, tag_id) VALUES
('2', '4'),
('3', '2'),
('3', '3'),
('5', '1'),
('5', '3'),
('7', '5'),
('8', '5'),
('12', '5');