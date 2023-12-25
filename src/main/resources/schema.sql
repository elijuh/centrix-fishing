CREATE TABLE IF NOT EXISTS `user_stats`(
    `uuid` BINARY(16),
    `key` VARCHAR(32),
    `value` INT NOT NULL,
    PRIMARY KEY(`uuid`, `key`)
);

CREATE TABLE IF NOT EXISTS `names`(
    `uuid` BINARY(16),
    `name` VARCHAR(16),
    PRIMARY KEY(`uuid`)
);