package com.coachapp.coach_pc.config;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Types;

public class CustomPostgresDialect extends PostgreSQL94Dialect {

    public CustomPostgresDialect() {
        super();
        this.registerHibernateType(Types.ARRAY, ListArrayType.class.getName());
        //this.registerHibernateType(2003, IntArrayType.class.getName());
        //this.registerColumnType(Types.ARRAY, "text[]");
        //this.registerColumnType(2003, "integer[]");
    }
}
