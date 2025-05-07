-- Insert authors
insert into authors (id, full_name)
values
    (1, 'Author_1'),
    (2, 'Author_2'),
    (3, 'Author_3');

-- Insert genres
insert into genres (id, name)
values
    (1, 'Genre_1'),
    (2, 'Genre_2'),
    (3, 'Genre_3');

-- Insert books, linked to authors and genres
insert into books (id, title, author_id, genre_id)
values
    (1, 'BookTitle_1', 1, 1),  -- Required for tests
    (2, 'BookTitle_2', 2, 2),
    (3, 'BookTitle_3', 3, 3);

-- Reset books auto_increment value
ALTER TABLE books ALTER COLUMN id RESTART WITH 4;

-- Insert comments, linked to books (sync with tests)
insert into comments (id, text, book_id)
values
    (1, 'Integration Test Comment', 1);  -- Required for tests

-- Reset comments auto_increment value
ALTER TABLE comments ALTER COLUMN id RESTART WITH 2; -- Since we only have id = 1