create table person (
    id uuid primary key,
    name varchar,
    passport varchar unique,
    qualification varchar
);

