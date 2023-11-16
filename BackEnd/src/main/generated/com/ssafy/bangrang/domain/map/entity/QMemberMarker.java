package com.ssafy.bangrang.domain.map.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberMarker is a Querydsl query type for MemberMarker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberMarker extends EntityPathBase<MemberMarker> {

    private static final long serialVersionUID = -1365609654L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberMarker memberMarker = new QMemberMarker("memberMarker");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final com.ssafy.bangrang.domain.member.entity.QAppMember appMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberMarker(String variable) {
        this(MemberMarker.class, forVariable(variable), INITS);
    }

    public QMemberMarker(Path<? extends MemberMarker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberMarker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberMarker(PathMetadata metadata, PathInits inits) {
        this(MemberMarker.class, metadata, inits);
    }

    public QMemberMarker(Class<? extends MemberMarker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appMember = inits.isInitialized("appMember") ? new com.ssafy.bangrang.domain.member.entity.QAppMember(forProperty("appMember")) : null;
    }

}

