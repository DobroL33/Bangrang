package com.ssafy.bangrang.domain.stamp.entity;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.stamp.entity.Stamp;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "app_member_stamp",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "app_member_stamp_unique",
                columnNames = {
                        "member_idx",
                        "stamp_idx"
                }
        )
    }
)
public class AppMemberStamp extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "app_member_stamp_idx")
    private Long appMemberStampIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_idx")
    private Stamp stamp;

    @Builder
    public AppMemberStamp(AppMember appMember, Stamp stamp){
        this.changeAppMember(appMember);
        this.changeStamp(stamp);
    }

    private void changeStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    private void changeAppMember(AppMember appMember) {
        this.appMember = appMember;
        appMember.getAppMemberStamps().add(this);
    }


}
