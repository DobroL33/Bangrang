package com.ssafy.bangrang.domain.rank.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRanking is a Querydsl query type for Ranking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanking extends EntityPathBase<Ranking> {

    private static final long serialVersionUID = -1686477654L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRanking ranking = new QRanking("ranking");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final com.ssafy.bangrang.domain.member.entity.QAppMember appMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final NumberPath<Double> percent = createNumber("percent", Double.class);

    public final NumberPath<Long> rank = createNumber("rank", Long.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final EnumPath<com.ssafy.bangrang.domain.map.model.vo.RegionType> regionType = createEnum("regionType", com.ssafy.bangrang.domain.map.model.vo.RegionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRanking(String variable) {
        this(Ranking.class, forVariable(variable), INITS);
    }

    public QRanking(Path<? extends Ranking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRanking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRanking(PathMetadata metadata, PathInits inits) {
        this(Ranking.class, metadata, inits);
    }

    public QRanking(Class<? extends Ranking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appMember = inits.isInitialized("appMember") ? new com.ssafy.bangrang.domain.member.entity.QAppMember(forProperty("appMember")) : null;
    }

}

