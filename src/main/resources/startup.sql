create  database bp_wallet;

use bp_wallet;

CREATE TABLE `bp_user_wallet` (
`user_id` VARCHAR(45) NOT NULL,
`currency` VARCHAR(3) NOT NULL,
`balance` FLOAT NULL,
PRIMARY KEY (`user_id`));

ALTER TABLE `bp_user_wallet` 
ADD INDEX `fk-currency_idx` (`currency` ASC) VISIBLE;
;

CREATE TABLE `bp_currency` (
`currency_id` VARCHAR(45) NOT NULL,
`currency_val` VARCHAR(45) NOT NULL,
PRIMARY KEY (`currency_id`),
UNIQUE INDEX `currency_val_UNIQUE` (`currency_val` ASC) VISIBLE);


ALTER TABLE `bp_user_wallet` 
ADD CONSTRAINT `fk-currency`
FOREIGN KEY (`currency`)
REFERENCES `bp_currency` (`currency_id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;


CREATE TABLE `bp_wallet`.`bp_user` (
  `user_id` VARCHAR(45) NOT NULL,
  `user_name` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`));

  
  CREATE TABLE `bp_wallet`.`bp_user_currency` (
  `user_currency_id` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(45) NULL,
  `currency_id` VARCHAR(45) NULL,
  PRIMARY KEY (`user_currency_id`),
  INDEX `fk-user-id_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_currency_id_idx` (`currency_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `bp_wallet`.`bp_user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_currency_id`
    FOREIGN KEY (`currency_id`)
    REFERENCES `bp_wallet`.`bp_currency` (`currency_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

	
	ALTER TABLE `bp_wallet`.`bp_user_wallet` 
DROP FOREIGN KEY `fk-currency`;
ALTER TABLE `bp_wallet`.`bp_user_wallet` 
DROP COLUMN `currency`,
CHANGE COLUMN `bp_wallet_id` `wallet_id` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `user_id` `user_currency_id` VARCHAR(45) NOT NULL ,
ADD INDEX `fk-user-currency_idx` (`user_currency_id` ASC) VISIBLE,
DROP INDEX `fk-currency_idx` ;
, RENAME TO  `bp_wallet`.`bp_wallet` ;
ALTER TABLE `bp_wallet`.`bp_user_wallet` 
ADD CONSTRAINT `fk-user-currency`
  FOREIGN KEY (`user_currency_id`)
  REFERENCES `bp_wallet`.`bp_user_currency` (`user_currency_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


INSERT INTO `bp_wallet`.`bp_currency`(`currency_id`,`currency_val`) VALUES(1,'USD');
INSERT INTO `bp_wallet`.`bp_currency`(`currency_id`,`currency_val`) VALUES(2,'EUR');
INSERT INTO `bp_wallet`.`bp_currency`(`currency_id`,`currency_val`) VALUES(3,'GBP');

commit;



INSERT INTO `bp_wallet`.`bp_user_wallet`(`user_id`,`currency`,`balance`) VALUES ('1','1',234.34);
INSERT INTO `bp_wallet`.`bp_user_wallet`(`user_id`,`currency`,`balance`) VALUES ('1','2',345.21);
INSERT INTO `bp_wallet`.`bp_user_wallet`(`user_id`,`currency`,`balance`) VALUES ('1','3',456.55);
