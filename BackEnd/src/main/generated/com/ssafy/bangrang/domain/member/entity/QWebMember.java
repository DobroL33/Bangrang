package com.ssafy.bangrang.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebMember is a Querydsl query type for WebMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWebMember extends EntityPathBase<WebMember> {

    private static final long serialVersionUID = -1547090284L;

    public static final QWebMember webMember = new QWebMember("webMember");

    public final QMember _super = new QMember(this);

    public final StringPath authFile = createString("authFile");

    public final ListPath<com.ssafy.bangrang.domain.inquiry.entity.Comment, com.ssafy.bangrang.domain.inquiry.entity.QComment> comments = this.<com.ssafy.bangrang.domain.inquiry.entity.Comment, com.ssafy.bangrang.domain.inquiry.entity.QComment>createList("comments", com.ssafy.bangrang.domain.inquiry.entity.Comment.class, com.ssafy.bangrang.domain.inquiry.entity.QComment.class, PathInits.DIRECT2);

    public final ListPath<com.ssafy.bangrang.domain.event.entity.Event, com.ssafy.bangrang.domain.event.entity.QEvent> events = this.<com.ssafy.bangrang.domain.event.entity.Event, com.ssafy.bangrang.domain.event.entity.QEvent>createList("events", com.ssafy.bangrang.domain.event.entity.Event.class, com.ssafy.bangrang.domain.event.entity.QEvent.class, PathInits.DIRECT2);

    //inherited
    public final StringPath id = _super.id;

    //inherited
    public final NumberPath<Long> idx = _super.idx;

    public final StringPath organizationName = createString("organizationName");

    //inherited
    public final StringPath password = _super.password;

    public final EnumPath<com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus> webMemberStatus = createEnum("webMemberStatus", com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus.class);

    public QWebMember(String variable) {
        super(WebMember.class, forVariable(variable));
    }

    public QWebMember(Path<? extends WebMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebMember(PathMetadata metadata) {
        super(WebMember.class, metadata);
    }

}

