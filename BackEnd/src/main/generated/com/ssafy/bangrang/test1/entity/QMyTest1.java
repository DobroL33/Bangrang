package com.ssafy.bangrang.test1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyTest1 is a Querydsl query type for MyTest1
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyTest1 extends EntityPathBase<MyTest1> {

    private static final long serialVersionUID = 1938679646L;

    public static final QMyTest1 myTest1 = new QMyTest1("myTest1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> position = createComparable("position", org.locationtech.jts.geom.Point.class);

    public final StringPath title = createString("title");

    public QMyTest1(String variable) {
        super(MyTest1.class, forVariable(variable));
    }

    public QMyTest1(Path<? extends MyTest1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyTest1(PathMetadata metadata) {
        super(MyTest1.class, metadata);
    }

}

