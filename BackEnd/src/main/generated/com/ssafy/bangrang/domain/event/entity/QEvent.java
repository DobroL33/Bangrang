package com.ssafy.bangrang.domain.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = 746686022L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final com.ssafy.bangrang.global.common.entity.QCommonEntity _super = new com.ssafy.bangrang.global.common.entity.QCommonEntity(this);

    public final StringPath address = createString("address");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final StringPath eventUrl = createString("eventUrl");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath image = createString("image");

    public final ListPath<com.ssafy.bangrang.domain.inquiry.entity.Inquiry, com.ssafy.bangrang.domain.inquiry.entity.QInquiry> inquiries = this.<com.ssafy.bangrang.domain.inquiry.entity.Inquiry, com.ssafy.bangrang.domain.inquiry.entity.QInquiry>createList("inquiries", com.ssafy.bangrang.domain.inquiry.entity.Inquiry.class, com.ssafy.bangrang.domain.inquiry.entity.QInquiry.class, PathInits.DIRECT2);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final ListPath<Likes, QLikes> likes = this.<Likes, QLikes>createList("likes", Likes.class, QLikes.class, PathInits.DIRECT2);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final com.ssafy.bangrang.domain.stamp.entity.QStamp stamp;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath subImage = createString("subImage");

    public final StringPath subTitle = createString("subTitle");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.ssafy.bangrang.domain.member.entity.QWebMember webMember;

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stamp = inits.isInitialized("stamp") ? new com.ssafy.bangrang.domain.stamp.entity.QStamp(forProperty("stamp"), inits.get("stamp")) : null;
        this.webMember = inits.isInitialized("webMember") ? new com.ssafy.bangrang.domain.member.entity.QWebMember(forProperty("webMember")) : null;
    }

}

