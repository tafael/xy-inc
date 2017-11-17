/** deve ser executado no banco de dados já criado. */

create sequence seq_model;
create sequence seq_field;

create table model (
	id integer not null default nextval('seq_model'),
	name character varying (255) not null,
	id_table_id integer,
	creation_date timestamp with time zone default now(),
	update_date timestamp with time zone default now(),
	constraint pk_model primary key (id),
	constraint unique_model_name unique (name)
);
create table field (
	id integer not null default nextval('seq_field'),
	name character varying (255) not null,
	type character varying (255) not null,
	size integer not null,
	not_null boolean not null default false,
	id_model integer not null,
	creation_date timestamp with time zone default now(),
	update_date timestamp with time zone default now(),
	constraint pk_field primary key (id),
	constraint fk_model foreign key (id_model) references model (id) on delete cascade,
	constraint unique_field_name unique (id_model, name)
);
alter table model add constraint fk_table_id foreign key (id_table_id) references field (id) on delete set null;