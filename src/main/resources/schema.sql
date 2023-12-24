CREATE TABLE IF NOT EXISTS `user_stats`(
    `uuid` BINARY(16),
    `key` VARCHAR(32),
    `value` INT NOT NULL,
    PRIMARY KEY(`uuid`, `key`)
);