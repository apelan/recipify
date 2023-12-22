INSERT INTO "users" ("email", "password", "created") VALUES (
    'user@recipify.com',
    '$2a$12$wZ90JiDNb3c1GelyrE8iTuBCfVJfFu/jkBfFPUu6j9/vxywZhjwwe',
    current_timestamp
);

INSERT INTO "users" ("email", "password", "created") VALUES (
    'user2@recipify.com',
    '$2a$12$wZ90JiDNb3c1GelyrE8iTuBCfVJfFu/jkBfFPUu6j9/vxywZhjwwe',
    current_timestamp
);


INSERT INTO "ingredients" ("name", "created") VALUES
('tomato', current_timestamp),
('carrot', current_timestamp),
('chicken', current_timestamp),
('salt', current_timestamp),
('eggs', current_timestamp),
('sausage', current_timestamp);


INSERT INTO "recipes" ("name", "description", "owner", "created")
VALUES ('chicken soup', 'lorem ipsum', 1, current_timestamp);

INSERT INTO recipe_ingredients("recipe_id", "ingredient_id") VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4);


INSERT INTO "recipes" ("name", "description", "owner", "created")
VALUES ('british breakfast', 'lorem ipsum', 2, current_timestamp);

INSERT INTO recipe_ingredients("recipe_id", "ingredient_id") VALUES
(2, 4),
(2, 5),
(2, 6);
