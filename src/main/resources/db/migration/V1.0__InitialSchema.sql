-- create table for users
CREATE TABLE IF NOT EXISTS "users" (
	"id" serial NOT NULL,
	"email" varchar(50) NOT NULL,
	"password" varchar(255) NOT NULL,
	"first_name" varchar(50),
	"last_name" varchar(50),
	"city" varchar(50),
	"country" varchar(50),
    "created" timestamp without time zone,
    "updated" timestamp without time zone,
	CONSTRAINT "users_pk" PRIMARY KEY ("id"),
	CONSTRAINT "users_email_uk" UNIQUE ("email")
);
-- add index on email column (this will be frequently used to fetch user)
CREATE INDEX IF NOT EXISTS "users_email_index" ON "users" ("email");


-- create table for recipes
CREATE TABLE IF NOT EXISTS "recipes" (
	"id" serial NOT NULL,
	"name" varchar(50) NOT NULL,
	"description" varchar(512) NOT NULL,
    "owner" integer NOT NULL,
    "created" timestamp without time zone,
    "updated" timestamp without time zone,
	CONSTRAINT "recipes_pk" PRIMARY KEY ("id"),
    CONSTRAINT "recipes_owner_fk" FOREIGN KEY ("owner") REFERENCES "users" ("id")
);


-- create table for ingredients
CREATE TABLE IF NOT EXISTS "ingredients" (
	"id" serial NOT NULL,
	"name" varchar(50) NOT NULL,
    "created" timestamp without time zone,
    "updated" timestamp without time zone,
	CONSTRAINT "ingredients_pk" PRIMARY KEY ("id")
);


-- create bridge table between recipes and ingredients
CREATE TABLE IF NOT EXISTS "recipe_ingredients" (
    "id" serial NOT NULL,
    "recipe_id" integer NOT NULL,
    "ingredient_id" integer NOT NULL,
    CONSTRAINT "recipe_ingredients_pk" PRIMARY KEY ("id"),
    CONSTRAINT "recipe_ingredients_recipes_fk" FOREIGN KEY ("recipe_id") REFERENCES "recipes" ("id"),
    CONSTRAINT "recipe_ingredients_ingredients_fk" FOREIGN KEY ("ingredient_id") REFERENCES "ingredients" ("id")
);


-- create table for storing recipe ratings
CREATE TABLE IF NOT EXISTS "recipe_ratings" (
    "id" serial NOT NULL,
    "recipe_id" integer NOT NULL,
    "user_id" integer NOT NULL,
    "rating" integer NOT NULL,
    "created" timestamp without time zone,
    "updated" timestamp without time zone,
    CONSTRAINT "user_recipes_pk" PRIMARY KEY ("id"),
    CONSTRAINT "user_recipes_recipes_fk" FOREIGN KEY ("recipe_id") REFERENCES "recipes" ("id"),
    CONSTRAINT "user_recipes_users_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
);
