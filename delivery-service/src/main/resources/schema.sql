DROP TABLE IF EXISTS P_DELIVERY_ROUTE;
DROP TABLE IF EXISTS P_DELIVERY;

CREATE TABLE P_DELIVERY
(
    DELIVERY_ID              BIGINT GENERATED BY DEFAULT AS IDENTITY
        PRIMARY KEY,
    DELIVERY_UUID            VARCHAR(36)           NOT NULL,
    ORDER_ID                 VARCHAR(255)          NOT NULL,
    ARRIVAL_HUB_ID           VARCHAR(255)          NOT NULL,
    DEPARTURE_HUB_ID         VARCHAR(255)          NOT NULL,
    DELIVERY_STATE           VARCHAR(10)           NOT NULL
        CONSTRAINT P_DELIVERY_DELIVERY_STATE_CHECK
        CHECK ((DELIVERY_STATE)::TEXT = ANY
        ((ARRAY ['PENDING'::CHARACTER VARYING, 'REQUESTED'::CHARACTER VARYING, 'IN_TRANSIT'::CHARACTER VARYING, 'DELIVERED'::CHARACTER VARYING, 'CONFIRMED'::CHARACTER VARYING])::TEXT[])),
    SHIPPING_MANGER_ID       VARCHAR(255),
    SHIPPING_MANGER_SLACK_ID VARCHAR(255),
    SHIPPING_ADDRESS         VARCHAR(255),
    SHIPPING_START_DATE      TIMESTAMP(6),
    SHIPPING_END_DATE        TIMESTAMP(6),
    ESTIMATED_DISTANCE       DOUBLE PRECISION,
    ESTIMATED_ELAPSED_TIME   BIGINT,
    IS_PUBLIC           BOOLEAN DEFAULT TRUE  NOT NULL,
    IS_DELETE           BOOLEAN DEFAULT FALSE NOT NULL,
    CREATED_AT          TIMESTAMP(6),
    CREATED_BY          VARCHAR(255),
    UPDATED_AT          TIMESTAMP(6),
    UPDATED_BY          VARCHAR(255),
    DELETED_AT          TIMESTAMP(6),
    DELETED_BY          VARCHAR(255)
);


CREATE TABLE P_DELIVERY_ROUTE
(
    DELIVERY_ROUTE_ID      BIGINT GENERATED BY DEFAULT AS IDENTITY
        PRIMARY KEY,
    DELIVERY_ID            BIGINT       NOT NULL,
    ARRIVAL_HUB_ID         VARCHAR(255) NOT NULL,
    DEPARTURE_HUB_ID       VARCHAR(255) NOT NULL,
    ESTIMATED_DISTANCE     DOUBLE PRECISION,
    ESTIMATED_ELAPSED_TIME BIGINT,
    REAL_ELAPSED_TIME      NUMERIC(21),
    SEQUENCE               BIGINT       NOT NULL,
    ROUTE_STATE            VARCHAR(22)
        CONSTRAINT P_DELIVERY_ROUTE_ROUTE_STATE_CHECK
        CHECK ((ROUTE_STATE)::TEXT = ANY
        ((ARRAY ['PENDING'::CHARACTER VARYING, 'IN_TRANSIT_TO_HUB'::CHARACTER VARYING, 'AT_ARRIVAL_HUB'::CHARACTER VARYING, 'OUT_FOR_DELIVERY'::CHARACTER VARYING, 'IN_TRANSIT_TO_COMPANY'::CHARACTER VARYING, 'DELIVERED'::CHARACTER VARYING])::TEXT[])),
    IS_PUBLIC           BOOLEAN DEFAULT TRUE  NOT NULL,
    IS_DELETE           BOOLEAN DEFAULT FALSE NOT NULL,
    CREATED_AT          TIMESTAMP(6),
    CREATED_BY          VARCHAR(255),
    UPDATED_AT          TIMESTAMP(6),
    UPDATED_BY          VARCHAR(255),
    DELETED_AT          TIMESTAMP(6),
    DELETED_BY          VARCHAR(255)
);
