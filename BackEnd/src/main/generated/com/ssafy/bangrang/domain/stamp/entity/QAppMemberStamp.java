package com.ssafy.bangrang.domain.stamp.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppMemberStamp is a Querydsl query type for AppMemberStamp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppMemberStamp extends EntityPathBase<AppMemberStamp> {

    private static final long serialVersionUID = -1159101787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppMemberStamp appMemberStamp = new QAppMemberStamp("appMemberStamp");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final com.ssafy.bangrang.domain.member.entity.QAppMember appMember;

    public final NumberPath<Long> appMemberStampIdx = createNumber("appMemberStampIdx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QStamp stamp;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAppMemberStamp(String variable) {
        this(AppMemberStamp.class, forVariable(variable), INITS);
    }

    public QAppMemberStamp(Path<? extends AppMemberStamp> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppMemberStamp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppMemberStamp(PathMetadata metadata, PathInits inits) {
        this(AppMemberStamp.class, metadata, inits);
    }

    public QAppMemberStamp(Class<? extends AppMemberStamp> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appMember = inits.isInitialized("appMember") ? new com.ssafy.bangrang.domain.member.entity.QAppMember(forProperty("appMember")) : null;
        this.stamp = inits.isInitialized("stamp") ? new QStamp(forProperty("stamp"), inits.get("stamp")) : null;
    }

}

