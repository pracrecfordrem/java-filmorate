CREATE TABLE if not exists users (
  id SERIAL NOT NULL,
  username varchar(40) NOT NULL,
  email varchar(255) NOT NULL,
  password varchar(40) NOT NULL,
  registration_date timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE if not exists friendship (
  id SERIAL NOT NULL,
  initiator_id int8 NOT NULL,
  recipient_id int8 NOT NULL,
  initiation_time timestamp NOT NULL,
  acception_date timestamp,
  acception_status bool DEFAULT false,
  PRIMARY KEY (id),
  CONSTRAINT friendship_initiator_id_fkey FOREIGN KEY (initiator_id) REFERENCES public.users(id),
  CONSTRAINT friendship_recipient_id_fkey FOREIGN KEY (recipient_id) REFERENCES public.users(id)
);

CREATE TABLE if not exists films (
  id SERIAL NOT NULL,
  title varchar(40) NOT NULL,
  produced int4 NOT NULL,
  date_prod date,
  kind varchar(10),
  len_min int4,
  genre varchar(255),
  PRIMARY KEY (id)
);


CREATE TABLE if not exists likes (
  id SERIAL NOT NULL,
  film_id int4 NOT NULL,
  user_id int8 NOT NULL,
  mark_time timestamp NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT likes_film_id_fkey FOREIGN KEY (film_id) REFERENCES public.films(id),
  CONSTRAINT likes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);