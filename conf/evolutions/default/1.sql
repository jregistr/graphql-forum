-- # --- !Ups
CREATE TABLE "users" (
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "first_name" VARCHAR(255) DEFAULT NULL,
  "last_name" VARCHAR(255) DEFAULT NULL,
  "email" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL
);

CREATE TABLE "forum_groups"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL
);

CREATE TABLE "forums"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "group" INTEGER NOT NULL,

  FOREIGN KEY ("group") REFERENCES "forum_groups"("id")
);

CREATE TABLE "threads"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "forum" INTEGER NOT NULL,
  "created_by" INTEGER NOT NULL,

  FOREIGN KEY ("forum") REFERENCES "forums"("id"),
  FOREIGN KEY ("created_by") REFERENCES "users"("id")
);


CREATE TABLE "posts"(
  "id" INTEGER PRIMARY KEY AUTO_INCREMENT,
  "date_created" TIMESTAMP NOT NULL,
  "last_modified" TIMESTAMP NOT NULL,
  "created_by" INTEGER NOT NULL,
  "text" CLOB NOT NULL,
  "replying_to" INTEGER DEFAULT NULL,

  FOREIGN KEY ("created_by") REFERENCES "users"("id"),
  FOREIGN KEY ("replying_to") REFERENCES "posts"("id")
);



-- # --- !Downs
-- DROP TABLE IF EXISTS admin;
