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
    (1, 'BookTitle_1', 1, 1),
    (2, 'BookTitle_2', 2, 2),
    (3, 'BookTitle_3', 3, 3);

-- Insert comments, linked to books
insert into comments (id, text, book_id)
values
    (1, 'Great book!', 1),
    (2, 'Not my cup of tea.', 1),
    (3, 'Brilliant read!', 2),
    (4, 'Could be better.', 3);