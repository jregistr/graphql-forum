-- # --- !Ups
CREATE TABLE "user" (
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "firstName" VARCHAR(255) DEFAULT NULL,
  "lastName" VARCHAR(255) DEFAULT NULL,
  "email" VARCHAR(255) DEFAULT NULL,
  "dateCreated" TIMESTAMP NOT NULL,
  "lastModified" TIMESTAMP NOT NULL
);

CREATE TABLE "forum_group"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL
);

CREATE TABLE "forum"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "group" INTEGER NOT NULL,

  FOREIGN KEY ("group") REFERENCES "forum_group"("id")
);

CREATE TABLE "thread"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "forum" INTEGER NOT NULL,
  "created_by" INTEGER NOT NULL,

  FOREIGN KEY ("forum") REFERENCES "forum"("id"),
  FOREIGN KEY ("created_by") REFERENCES "user"("id")
);


CREATE TABLE "post"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "created_by" INTEGER NOT NULL,
  "text" CLOB NOT NULL,
  "replying_to" INTEGER DEFAULT NULL,

  FOREIGN KEY ("created_by") REFERENCES "user"("id"),
  FOREIGN KEY ("replying_to") REFERENCES "post"("id")
);



-- # --- !Downs
-- DROP TABLE IF EXISTS admin;
