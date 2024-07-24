CREATE TABLE conta
(
    id bigint NOT NULL,
    data_vencimento date NOT NULL,
    data_pagamento date,
    descricao character varying(255) COLLATE pg_catalog."default",
    situacao boolean,
    valor numeric(38,2) NOT NULL
);