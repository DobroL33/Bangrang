package com.ssafy.bangrang.domain.stamp.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStamp is a Querydsl query type for Stamp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStamp extends EntityPathBase<Stamp> {

    private static final long serialVersionUID = -464359194L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStamp stamp = new QStamp("stamp");

    public final com.ssafy.bangrang.domain.event.entity.QEvent event;

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath name = createString("name");

    public QStamp(String variable) {
        this(Stamp.class, forVariable(variable), INITS);
    }

    public QStamp(Path<? extends Stamp> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStamp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStamp(PathMetadata metadata, PathInits inits) {
        this(Stamp.class, metadata, inits);
    }

    public QStamp(Class<? extends Stamp> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new com.ssafy.bangrang.domain.event.entity.QEvent(forProperty("event"), inits.get("event")) : null;
    }

}

