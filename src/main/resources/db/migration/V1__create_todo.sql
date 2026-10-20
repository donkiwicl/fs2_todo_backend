CREATE TABLE todo (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(30)  NOT NULL,
    descripcion VARCHAR(256),
    finalizado  BOOLEAN      NOT NULL DEFAULT FALSE
);
