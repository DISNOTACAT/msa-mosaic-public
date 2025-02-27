DROP TABLE IF EXISTS P_AI_PROMPT_LOGS;
DROP TABLE IF EXISTS P_AI_PROMPT_TEMPLATES;


-- AUTO-GENERATED DEFINITION
CREATE TABLE P_AI_PROMPT_LOGS
(
    AI_ID         BIGINT GENERATED  BY DEFAULT AS IDENTITY    PRIMARY KEY,
    AI_UUID       VARCHAR(36)  NOT NULL   UNIQUE,
    USER_PROMPT   TEXT         NOT NULL,
    AI_RESPONSE   TEXT         NOT NULL,
    AI_MODEL_NAME VARCHAR(255) NOT NULL,
    IS_DELETED    BOOLEAN      NOT NULL,
    IS_PUBLIC     BOOLEAN      NOT NULL,
    CREATED_AT    TIMESTAMP(6) NOT NULL,
    CREATED_BY    VARCHAR(255),
    UPDATED_AT    TIMESTAMP(6),
    UPDATED_BY    VARCHAR(255),
    DELETED_AT    TIMESTAMP(6),
    DELETED_BY    VARCHAR(255)
);

ALTER TABLE P_AI_PROMPT_LOGS
    OWNER TO POSTGRE;

--

-- AUTO-GENERATED DEFINITION
CREATE TABLE P_AI_PROMPT_TEMPLATES
(
    TEMPLATE_ID   BIGINT GENERATED BY DEFAULT AS IDENTITY    PRIMARY KEY,
    TEMPLATE_UUID VARCHAR(36)  NOT NULL    UNIQUE,
    USER_PROMPT   TEXT         NOT NULL,
    IS_PUBLIC     BOOLEAN      NOT NULL,
    IS_DELETED    BOOLEAN      NOT NULL,
    CREATED_AT    TIMESTAMP(6) NOT NULL,
    CREATED_BY    VARCHAR(255),
    UPDATED_AT    TIMESTAMP(6),
    UPDATED_BY    VARCHAR(255),
    DELETED_AT    TIMESTAMP(6),
    DELETED_BY    VARCHAR(255)
);

ALTER TABLE P_AI_PROMPT_TEMPLATES
    OWNER TO POSTGRE;


