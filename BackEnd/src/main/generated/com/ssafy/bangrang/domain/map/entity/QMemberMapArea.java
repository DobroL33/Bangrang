package com.ssafy.bangrang.domain.map.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberMapArea is a Querydsl query type for MemberMapArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberMapArea extends EntityPathBase<MemberMapArea> {

    private static final long serialVersionUID = 612687609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberMapArea memberMapArea = new QMemberMapArea("memberMapArea");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final com.ssafy.bangrang.domain.member.entity.QAppMember appMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Double> dimension = createNumber("dimension", Double.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final EnumPath<com.ssafy.bangrang.domain.map.model.vo.RegionType> regionType = createEnum("regionType", com.ssafy.bangrang.domain.map.model.vo.RegionType.class);

    public final ComparablePath<org.locationtech.jts.geom.Geometry> shape = createComparable("shape", org.locationtech.jts.geom.Geometry.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberMapArea(String variable) {
        this(MemberMapArea.class, forVariable(variable), INITS);
    }

    public QMemberMapArea(Path<? extends MemberMapArea> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberMapArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberMapArea(PathMetadata metadata, PathInits inits) {
        this(MemberMapArea.class, metadata, inits);
    }

    public QMemberMapArea(Class<? extends MemberMapArea> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appMember = inits.isInitialized("appMember") ? new com.ssafy.bangrang.domain.member.entity.QAppMember(forProperty("appMember")) : null;
    }

}

