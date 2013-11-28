# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tag (
  id                        varchar(255) not null,
  nombre                    varchar(255),
  constraint pk_tag primary key (id))
;

create table url (
  id                        bigint not null,
  nombre                    varchar(255),
  user_id                   bigint,
  constraint pk_url primary key (id))
;

create table usuario (
  id                        bigint not null,
  nombre                    varchar(255),
  nick                      varchar(255),
  constraint pk_usuario primary key (id))
;


create table url_tag (
  url_id                         bigint not null,
  tag_id                         varchar(255) not null,
  constraint pk_url_tag primary key (url_id, tag_id))
;
create sequence tag_seq;

create sequence url_seq;

create sequence usuario_seq;

alter table url add constraint fk_url_user_1 foreign key (user_id) references usuario (id) on delete restrict on update restrict;
create index ix_url_user_1 on url (user_id);



alter table url_tag add constraint fk_url_tag_url_01 foreign key (url_id) references url (id) on delete restrict on update restrict;

alter table url_tag add constraint fk_url_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists tag;

drop table if exists url_tag;

drop table if exists url;

drop table if exists usuario;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists tag_seq;

drop sequence if exists url_seq;

drop sequence if exists usuario_seq;

