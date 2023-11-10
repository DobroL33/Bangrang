package com.ssafy.bangrang.domain.rank.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRanking is a Querydsl query type for Ranking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanking extends EntityPathBase<Ranking> {

    private static final long serialVersionUID = -1686477654L;

    public static final QRanking ranking = new QRanking("ranking");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final NumberPath<Long> appMember = createNumber("appMember", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final NumberPath<Long> rank = createNumber("rank", Long.class);

    public final EnumPath<com.ssafy.bangrang.domain.map.model.vo.RegionType> regionType = createEnum("regionType", com.ssafy.bangrang.domain.map.model.vo.RegionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRanking(String variable) {
        super(Ranking.class, forVariable(variable));
    }

    public QRanking(Path<? extends Ranking> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRanking(PathMetadata metadata) {
        super(Ranking.class, metadata);
    }

}

