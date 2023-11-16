package com.ssafy.bangrang.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppMember is a Querydsl query type for AppMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppMember extends EntityPathBase<AppMember> {

    private static final long serialVersionUID = 1093396385L;

    public static final QAppMember appMember = new QAppMember("appMember");

    public final QMember _super = new QMember(this);

    public final BooleanPath alarms = createBoolean("alarms");

    public final ListPath<com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp, com.ssafy.bangrang.domain.stamp.entity.QAppMemberStamp> appMemberStamps = this.<com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp, com.ssafy.bangrang.domain.stamp.entity.QAppMemberStamp>createList("appMemberStamps", com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp.class, com.ssafy.bangrang.domain.stamp.entity.QAppMemberStamp.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath firebaseToken = createString("firebaseToken");

    public final ListPath<Friendship, QFriendship> friendships = this.<Friendship, QFriendship>createList("friendships", Friendship.class, QFriendship.class, PathInits.DIRECT2);

    //inherited
    public final StringPath id = _super.id;

    //inherited
    public final NumberPath<Long> idx = _super.idx;

    public final StringPath imgUrl = createString("imgUrl");

    public final ListPath<com.ssafy.bangrang.domain.inquiry.entity.Inquiry, com.ssafy.bangrang.domain.inquiry.entity.QInquiry> inquiries = this.<com.ssafy.bangrang.domain.inquiry.entity.Inquiry, com.ssafy.bangrang.domain.inquiry.entity.QInquiry>createList("inquiries", com.ssafy.bangrang.domain.inquiry.entity.Inquiry.class, com.ssafy.bangrang.domain.inquiry.entity.QInquiry.class, PathInits.DIRECT2);

    public final ListPath<com.ssafy.bangrang.domain.event.entity.Likes, com.ssafy.bangrang.domain.event.entity.QLikes> likes = this.<com.ssafy.bangrang.domain.event.entity.Likes, com.ssafy.bangrang.domain.event.entity.QLikes>createList("likes", com.ssafy.bangrang.domain.event.entity.Likes.class, com.ssafy.bangrang.domain.event.entity.QLikes.class, PathInits.DIRECT2);

    public final ListPath<com.ssafy.bangrang.domain.map.entity.MemberMapArea, com.ssafy.bangrang.domain.map.entity.QMemberMapArea> memberMapAreas = this.<com.ssafy.bangrang.domain.map.entity.MemberMapArea, com.ssafy.bangrang.domain.map.entity.QMemberMapArea>createList("memberMapAreas", com.ssafy.bangrang.domain.map.entity.MemberMapArea.class, com.ssafy.bangrang.domain.map.entity.QMemberMapArea.class, PathInits.DIRECT2);

    public final ListPath<com.ssafy.bangrang.domain.map.entity.MemberMarker, com.ssafy.bangrang.domain.map.entity.QMemberMarker> memberMarkers = this.<com.ssafy.bangrang.domain.map.entity.MemberMarker, com.ssafy.bangrang.domain.map.entity.QMemberMarker>createList("memberMarkers", com.ssafy.bangrang.domain.map.entity.MemberMarker.class, com.ssafy.bangrang.domain.map.entity.QMemberMarker.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    //inherited
    public final StringPath password = _super.password;

    public QAppMember(String variable) {
        super(AppMember.class, forVariable(variable));
    }

    public QAppMember(Path<? extends AppMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppMember(PathMetadata metadata) {
        super(AppMember.class, metadata);
    }

}

