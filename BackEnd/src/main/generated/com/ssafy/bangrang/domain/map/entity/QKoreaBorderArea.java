package com.ssafy.bangrang.domain.map.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKoreaBorderArea is a Querydsl query type for KoreaBorderArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKoreaBorderArea extends EntityPathBase<KoreaBorderArea> {

    private static final long serialVersionUID = 765884301L;

    public static final QKoreaBorderArea koreaBorderArea = new QKoreaBorderArea("koreaBorderArea");

    public final StringPath bjcd = createString("bjcd");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath name = createString("name");

    public final ComparablePath<org.locationtech.jts.geom.MultiPolygon> shape = createComparable("shape", org.locationtech.jts.geom.MultiPolygon.class);

    public QKoreaBorderArea(String variable) {
        super(KoreaBorderArea.class, forVariable(variable));
    }

    public QKoreaBorderArea(Path<? extends KoreaBorderArea> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKoreaBorderArea(PathMetadata metadata) {
        super(KoreaBorderArea.class, metadata);
    }

}

