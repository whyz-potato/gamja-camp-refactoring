insert into member (account, picture, role, username, member_id) values ('campOwner@test.com', NULL, 'OWNER', 'owner', 1);

insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 광진구 능동로 209', '캠프소개', NULL, NULL, 37.5507345, 127.0741314, 1, '캠핑의 정석', '010-1234-1234', 2);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 마포구 와우산로21길 21-16', '캠프소개', NULL, NULL, 37.5516821, 126.9221623, 1, '캠핑 민족', '010-1234-1234', 3);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 용산구 백범로99길 50', '캠프소개', NULL, NULL, 37.5360397, 126.972224, 1, '놀러와요 동물의 숲', '010-1234-1234', 4);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 강남구 도산대로15길 32-10', '캠프소개', NULL, NULL, 37.5209049, 127.0235436, 1, '서울 캠핑 1호점', '010-1234-1234', 5);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 강남구 봉은사로18길 80', '캠프소개', NULL, NULL, 37.5015718, 127.0273738, 1, '서울 캠핑 2호점', '010-1234-1234', 6);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 영등포구 경인로79길 17-2', '캠프소개', NULL, NULL, 37.5133211, 126.8933711, 1, '고기굽는 캠핑장', '010-1234-1234', 7);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 마포구 성미산로 161-15', '캠프소개', NULL, NULL, 37.5653218, 126.9235254, 1, '와요', '010-1234-1234', 8);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 종로구 경희궁길 41', '캠프소개', NULL, NULL, 37.5729273, 126.9692798, 1, '오늘캠핑', '010-1234-1234', 9);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 동작구 동작대로13길 6-7', '캠프소개', NULL, NULL, 37.4820582, 126.9811174, 1, '중앙캠핑', '010-1234-1234', 10);

insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 2, 1, '객실1', 15000, 20000, 11);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 2, 1, '객실2', 15000, 20000, 12);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 4, 1, '객실3', 25000, 30000, 13);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 4, 1, '객실4', 25000, 30000, 14);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 2, 3, '2인', 15000, 20000, 15);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 4, 3, '4인', 25000, 30000, 16);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 6, 1, '6인', 50000, 80000, 17);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 2, 1, 'typeA', 15000, 20000, 18);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 2, 1, 'typeB', 15000, 20000, 19);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 4, 1, 'typeC', 25000, 30000, 20);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 4, 1, 'typeD', 25000, 30000, 21);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (5, 2, 3, '2인', 15000, 20000, 22);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (6, 4, 3, '4인', 25000, 30000, 23);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (7, 6, 1, '6인', 50000, 80000, 24);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (8, 2, 1, 'typeA', 15000, 20000, 25);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (9, 2, 1, 'typeB', 15000, 20000, 26);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (10, 10, 1, '단체1', 150000, 250000, 27);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (10, 10, 1, '단체2', 150000, 250000, 28);


