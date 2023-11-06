package com.ssafy.bangrang.spatial.map.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyTest is a Querydsl query type for MyTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyTest extends EntityPathBase<MyTest> {

    private static final long serialVersionUID = 1856456390L;

    public static final QMyTest myTest = new QMyTest("myTest");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QMyTest(String variable) {
        super(MyTest.class, forVariable(variable));
    }

    public QMyTest(Path<? extends MyTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyTest(PathMetadata metadata) {
        super(MyTest.class, metadata);
    }

}

