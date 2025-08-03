create table if not exists genres (
	genre_id int auto_increment primary key,
	genre_name varchar(255) not null
);

create table if not exists mpa_ratings (
	mpa_rating_id int auto_increment primary key,
	mpa_rating_name varchar(255) not null
);

create table if not exists users (
	user_id bigint auto_increment primary key,
	name varchar(255) not null,
	email varchar(255) not null unique,
	login varchar(255) not null unique,
	birthday date not null
);

create table if not exists films (
	film_id bigint auto_increment primary key,
	name varchar(255) not null,
	description varchar(200) null,
	releaseDate date null,
	duration int null,
	mpa_rating_id int null,
	foreign key (mpa_rating_id) references mpa_ratings (mpa_rating_id) on delete set null
);

create table if not exists films_genres (
	film_id bigint not null,
	genre_id int not null,
	primary key (film_id, genre_id),
	foreign key (film_id) references films (film_id),
	foreign key (genre_id) references genres (genre_id)
);

create table if not exists friends (
	user_id bigint not null,
	friend_id bigint not null,
	friendship_state boolean default false,
	primary key (user_id, friend_id),
	foreign key (user_id) references users (user_id) on delete cascade,
	foreign key (friend_id) references users (user_id) on delete cascade
);

create table if not exists likes (
	film_id bigint not null,
	user_id bigint not null,
	primary key (film_id, user_id),
	foreign key (film_id) references films (film_id) on delete cascade,
	foreign key (user_id) references users (user_id) on delete cascade
);

