-- Создание таблиц
create table authors (
     id bigint auto_increment,
     full_name varchar(255) not null,
     primary key (id)
);

create table genres (
    id bigint auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table books (
   id bigint auto_increment,
   title varchar(255) not null,
   author_id bigint not null references authors (id) on delete cascade,
   genre_id bigint not null references genres (id) on delete cascade,
   primary key (id)
);

create table comments (
      id bigint auto_increment,
      text varchar(500) not null,
      book_id bigint not null references books (id) on delete cascade,
      primary key (id)
);