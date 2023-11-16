package com.ssafy.bangrang.test2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title"})
public class MyTest2 {
    @Id
    @GeneratedValue
    Long id;

    String title;

    @Builder
    public MyTest2(String title){
        this.title = title;
    }
}
