-- # --- !Ups
CREATE TABLE "profile" (
  "id"        INTEGER PRIMARY KEY AUTOINCREMENT,
  "firstName" VARCHAR,
  "lastName"  VARCHAR,
  "email"     VARCHAR
);

# --- !Downs
DROP TABLE IF EXISTS admin;
