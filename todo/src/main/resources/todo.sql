CREATE TABLE actions (
    id         BIGINT AUTO_INCREMENT,
    active     BOOLEAN      NOT NULL,
    title      VARCHAR(255) NOT NULL,
    project_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT actions_unique_project_id_title UNIQUE (project_id, title),
    CONSTRAINT FK_actions_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE closed_days (
    obs        DATE   NOT NULL,
    project_id BIGINT NOT NULL,
    PRIMARY KEY (obs, project_id),
    CONSTRAINT FK_closed_days_project_id FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE entries (
    id         BIGINT AUTO_INCREMENT,
    created_at TIMESTAMP,
    hours      FLOAT       NOT NULL,
    closed     BOOLEAN     NOT NULL,
    obs        DATE        NOT NULL,
    title      VARCHAR(255) NOT NULL,
    update_at  TIMESTAMP,
    action_id  BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_entries_action_id FOREIGN KEY (action_id) REFERENCES actions (id),
    CONSTRAINT FK_entries_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE permissions (
    id   BIGINT AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT permissions_unique_code UNIQUE (code)
);

CREATE TABLE project_roles (
    project_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT FK_project_roles_project_id FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT FK_project_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_project_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE projects (
    id     BIGINT AUTO_INCREMENT,
    active BOOLEAN      NOT NULL,
    title  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT projects_unique_title UNIQUE (title)
);

CREATE TABLE roles (
    id          BIGINT AUTO_INCREMENT,
    code        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT roles_unique_code UNIQUE (code),
    CONSTRAINT roles_unique_title UNIQUE (title)
);

CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT,
    email      VARCHAR(255) NOT NULL,
    validated  BOOLEAN      NOT NULL,
    name       VARCHAR(255) NOT NULL,
    week_start INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT users_unique_email UNIQUE (email)
);

CREATE TABLE roles_permissions (
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT FK_roles_permissions_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT FK_roles_permissions_permission_id FOREIGN KEY (permission_id) REFERENCES permissions (id)
);