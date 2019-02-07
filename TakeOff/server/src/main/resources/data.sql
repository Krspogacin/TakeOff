INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_SYS_ADMIN');
INSERT INTO AUTHORITY (id, name) VALUES (3, 'ROLE_AIRCOMPANY_ADMIN');
INSERT INTO AUTHORITY (id, name) VALUES (4, 'ROLE_HOTEL_ADMIN');
INSERT INTO AUTHORITY (id, name) VALUES (5, 'ROLE_RENTACAR_ADMIN');

INSERT INTO `takeoff`.`administrator`(`password`,`username`,`authority_id`) VALUES ('$2a$10$W07Myhd5tmmRA1ohf92vce0Vae8Xtgjtk9M.1md/dHf2AUR1zzNXm','sys_admin', 2);

INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('1', 'Transfer to/from the airport');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('2', 'Parking');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('3', 'Swimming pool');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('4', 'Breakfast');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('5', 'Lunch');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('6', 'Dinner');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('7', 'Room service');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('8', 'Welness');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('9', 'Spa center');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('10', 'WiFi');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('11', 'Gym');
INSERT INTO `takeoff`.`service` (`id`, `name`) VALUES ('12', 'Sports fields');