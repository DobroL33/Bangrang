package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stamp")
public class Stamp{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stamp_idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_idx", nullable = false)
    private Event event;

    @JoinColumn(name = "stamp_name")
    private String name;

    @Builder
    public Stamp(Event event, String name){
        this.changeEvent(event);
        this.name = name;
    }

    private void changeEvent(Event event) {
        this.event = event;
        event.getStamps().add(this);
    }
}
