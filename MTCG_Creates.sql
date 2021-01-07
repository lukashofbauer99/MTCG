CREATE TABLE Users
(
    ID       SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --Credentials
    username varchar            NOT NULL UNIQUE,
    password varchar            NOT NULL,
    --editableUserData
    name     varchar,
    image    varchar,
    bio      varchar,
    --in user
    coins    int                NOT NULL,
    mmr      int                NOT NULL
);

CREATE TABLE Effects
(
    ID           SERIAL PRIMARY KEY NOT NULL UNIQUE,
    type         varchar            NOT NULL UNIQUE,
    name         varchar            NOT NULL,
    --foreign key
    baseEffectID int,
    FOREIGN KEY (baseEffectID) REFERENCES Effects (id)
);

CREATE TABLE Races
(
    ID         SERIAL PRIMARY KEY NOT NULL UNIQUE,
    type       varchar            NOT NULL UNIQUE,
    name       varchar            NOT NULL,
    --foreign key
    baseRaceID int,
    FOREIGN KEY (baseRaceID) REFERENCES Races (id)
);

CREATE TABLE Cards
(
    ID       varchar PRIMARY KEY NOT NULL UNIQUE,
    name     varchar             NOT NULL,
    damage   float4              NOT NULL,
    type     varchar             NOT NULL,
    --foreign keys
    effectID int,
    FOREIGN KEY (effectID) REFERENCES Effects (ID),
    raceID   int,
    FOREIGN KEY (raceID) REFERENCES Races (ID)
);

CREATE TABLE Decks
(
    ID     SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --foreign keys
    userID int                NOT NULL,
    FOREIGN KEY (userID) REFERENCES Users (id) ON DELETE CASCADE,
    cardID varchar,
    FOREIGN KEY (cardID) REFERENCES Cards (id) ON DELETE CASCADE
);

CREATE TABLE Stacks
(
    ID     SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --foreign keys
    userID int                NOT NULL,
    FOREIGN KEY (userID) REFERENCES Users (id) ON DELETE CASCADE,

    cardID varchar,
    FOREIGN KEY (cardID) REFERENCES Cards (id) ON DELETE CASCADE
);

CREATE TABLE NormalTrades
(
    ID            varchar PRIMARY KEY NOT NULL UNIQUE,
    --region Requirements
    minimumDamage float4,
    cardType      varchar,
    --foreign keys
    effectID      int,
    FOREIGN KEY (effectID) REFERENCES Effects (id),
    raceID        int,
    FOREIGN KEY (raceID) REFERENCES Races (id),
    --endregion
    --foreign keys
    userID        int,
    FOREIGN KEY (userID) REFERENCES Users (id) ON DELETE CASCADE,
    cardID        varchar,
    FOREIGN KEY (cardID) REFERENCES Cards (id)
);

CREATE TABLE Vendors
(
    ID SERIAL PRIMARY KEY NOT NULL UNIQUE,
    type       varchar
);

CREATE TABLE CardPacks
(
    ID         SERIAL PRIMARY KEY NOT NULL UNIQUE,
    cardAmount int,
    costs      int,
    packType   varchar,
    type       varchar,
    --foreign keys
    vendorId   int,
    FOREIGN KEY (vendorId) REFERENCES Vendors (ID)

);

CREATE TABLE CardsInPack
(
    ID         SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --foreign keys
    cardPackID int,
    FOREIGN KEY (cardPackID) REFERENCES CardPacks (ID) ON DELETE CASCADE ,
    cardID     varchar,
    FOREIGN KEY (cardID) REFERENCES Cards (ID) ON DELETE CASCADE
);


CREATE TABLE Battles
(
    ID     SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --foreign keys
    user1  int,
    FOREIGN KEY (user1) REFERENCES Users (ID) ON DELETE CASCADE,
    user2  int,
    FOREIGN KEY (user2) REFERENCES Users (ID) ON DELETE CASCADE,
    winner int,
    FOREIGN KEY (winner) REFERENCES Users (ID)

);
CREATE TABLE Rounds
(
    ID           SERIAL PRIMARY KEY NOT NULL UNIQUE,
    roundOutcome varchar,
    winnerDmg    float4,
    looserDmg    float4,
    --foreign keys
    winnerCardID varchar,
    FOREIGN KEY (winnerCardID) REFERENCES Cards (ID),
    looserCardID varchar,
    FOREIGN KEY (looserCardID) REFERENCES Cards (ID),
    battleID int,
    FOREIGN KEY (battleID) REFERENCES Battles (id) ON DELETE CASCADE

);

CREATE TABLE BattleDecks
(
    ID       SERIAL PRIMARY KEY NOT NULL UNIQUE,
    --foreign keys
    battleID int,
    FOREIGN KEY (battleID) REFERENCES Battles (id) ON DELETE CASCADE,
    cardID   varchar,
    FOREIGN KEY (cardID) REFERENCES Cards (ID) ON DELETE CASCADE
);


