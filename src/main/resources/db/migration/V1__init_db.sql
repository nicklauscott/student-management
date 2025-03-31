
    create sequence course_seq start with 1 increment by 50;

    create sequence student_seq start with 1 increment by 50;

    create table course (
        credit_hours integer not null,
        created_at timestamp(6),
        id bigint not null,
        last_modified_at timestamp(6),
        course_code varchar(255),
        course_name varchar(255),
        created_by varchar(255),
        department varchar(255),
        description varchar(255),
        last_modified_by varchar(255),
        prerequisites varchar(255) array,
        primary key (id)
    );

    create table student (
        created_at timestamp(6),
        date_of_birth timestamp(6),
        enrollment_date timestamp(6),
        id bigint not null,
        last_modified_at timestamp(6),
        address varchar(255),
        created_by varchar(255),
        department varchar(255),
        email varchar(255),
        first_name varchar(255) not null,
        gender varchar(255) check (gender in ('MALE','FEMALE','OTHER')),
        guardian_mobile varchar(255) not null,
        last_modified_by varchar(255),
        last_name varchar(255) not null,
        program varchar(255),
        status varchar(255) check (status in ('ACTIVE','GRADUATED','SUSPENDED','DROPPED_OUT')),
        primary key (id)
    );

    create table student_courses (
        course_id bigint not null,
        student_id bigint not null
    );

    alter table if exists student_courses
       add constraint FKc614in0kdhj9sih7vw304qxgj
       foreign key (course_id)
       references course;

    alter table if exists student_courses
       add constraint FKiqufqwgb6im4n8xslhjvxmt0n
       foreign key (student_id)
       references student;
