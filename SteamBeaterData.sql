DROP DATABASE IF EXISTS SteamBeaterData;
CREATE DATABASE SteamBeaterData;
USE SteamBeaterData;

CREATE TABLE GameList (
entryID varchar(30),
gameID varchar(10),
userID varchar(20),
beaten TINYINT(1),
unbeatable TINYINT(1),
ignored TINYINT(1),
PRIMARY KEY (entryID)
);
