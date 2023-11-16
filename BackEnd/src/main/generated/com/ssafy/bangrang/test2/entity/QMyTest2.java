package com.ssafy.bangrang.test2.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyTest2 is a Querydsl query type for MyTest2
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyTest2 extends EntityPathBase<MyTest2> {

    private static final long serialVersionUID = 1428145470L;

    public static final QMyTest2 myTest2 = new QMyTest2("myTest2");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QMyTest2(String variable) {
        super(MyTest2.class, forVariable(variable));
    }

    public QMyTest2(Path<? extends MyTest2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyTest2(PathMetadata metadata) {
        super(MyTest2.class, metadata);
    }

}

