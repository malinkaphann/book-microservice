
CREATE TABLE IF NOT EXISTS "user"(
    "id"        SERIAL NOT NULL,
    "username"  VARCHAR(128) NOT NULL,
    "password"  VARCHAR(1024) NOT NULL
);
ALTER TABLE "user" ADD PRIMARY KEY("id");
ALTER TABLE "user" ADD CONSTRAINT "user_unique_username" UNIQUE("username");
CREATE INDEX user_index_username ON "user"(username);

CREATE TABLE IF NOT EXISTS "role"(
    "id"    SERIAL NOT NULL,
    "name"  VARCHAR(32) NOT NULL
);
ALTER TABLE "role" ADD PRIMARY KEY("id");
ALTER TABLE "role" ADD CONSTRAINT "role_unique_name" UNIQUE("name");
CREATE INDEX role_index_name ON "role"(name);

CREATE TABLE IF NOT EXISTS "user_role"(
    "id"       SERIAL NOT NULL,
    "user_id"  INTEGER NOT NULL,
    "role_id"  INTEGER NOT NULL
);
ALTER TABLE "user_role" ADD PRIMARY KEY("id");
ALTER TABLE "user_role" ADD CONSTRAINT "user_role_to_user" FOREIGN KEY("user_id") REFERENCES "user"("id");
ALTER TABLE "user_role" ADD CONSTRAINT "user_role_to_role" FOREIGN KEY("role_id") REFERENCES "role"("id");
CREATE INDEX user_role_index ON "user_role"(user_id, role_id);

CREATE TABLE IF NOT EXISTS "user_profile"(
    "id"       SERIAL NOT NULL,
    "name"     VARCHAR(32) NOT NULL,
    "phone"    VARCHAR(32) NOT NULL,
    "email"    VARCHAR(32) NOT NULL,
    "photo"    VARCHAR(128) NULL,
    "student_id" VARCHAR(32) NULL,
    "user_id"  INTEGER NOT NULL
);

ALTER TABLE "user_profile" ADD PRIMARY KEY("id");
ALTER TABLE "user_profile" ADD CONSTRAINT "user_profile_unique_email" UNIQUE("email");
ALTER TABLE "user_profile" ADD CONSTRAINT "user_profile_unique_user_id" UNIQUE("user_id");
ALTER TABLE "user_profile" ADD CONSTRAINT "user_profile_to_user" FOREIGN KEY("user_id") REFERENCES "user"("id");
CREATE INDEX user_profile_index_name ON "user_profile"(name);
CREATE INDEX user_profile_index_email ON "user_profile"(email);
CREATE INDEX user_profile_index_student_id ON "user_profile"(student_id);
CREATE INDEX user_profile_index_user_id ON "user_profile"(user_id);

CREATE TABLE IF NOT EXISTS "book"(
    "id"           SERIAL NOT NULL,
    "code"         VARCHAR(128) NOT NULL,
    "author"       VARCHAR(64) NOT NULL,
    "title"        VARCHAR(64) NOT NULL,
    "category"     VARCHAR(32) CHECK("category" IN('STUDY', 'COMIC', 'NOVEL')) NOT NULL DEFAULT 'STUDY',
    "status"     VARCHAR(10) CHECK("status" IN('GOOD','OLD','DELETED')) NOT NULL DEFAULT 'GOOD',
    "description"  TEXT NULL
);
ALTER TABLE "book" ADD PRIMARY KEY("id");
ALTER TABLE "book" ADD CONSTRAINT "book_unique_code" UNIQUE("code");
CREATE INDEX idx_book_author ON "book"(author);
CREATE INDEX idx_book_title ON "book"(title);
CREATE INDEX idx_book_category ON "book"(category);

CREATE TABLE IF NOT EXISTS "user_hold_book"(
    "id"         SERIAL NOT NULL,
    "user_id"    INTEGER NOT NULL,
    "book_id"    INTEGER NOT NULL,
    "created_at" TIMESTAMP(0) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE "user_hold_book" ADD PRIMARY KEY("id");
ALTER TABLE "user_hold_book" ADD CONSTRAINT "user_hold_bookto_user" FOREIGN KEY("user_id") REFERENCES "user"("id");
ALTER TABLE "user_hold_book" ADD CONSTRAINT "user_hold_book_to_book" FOREIGN KEY("book_id") REFERENCES "book"("id");
CREATE INDEX user_hold_book_index_user ON user_hold_book(user_id, book_id);