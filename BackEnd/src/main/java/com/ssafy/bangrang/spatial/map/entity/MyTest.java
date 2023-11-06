package com.ssafy.bangrang.spatial.map.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class MyTest {
    @Id
    @GeneratedValue
    Long id;

    String title;

    @Builder
    public MyTest(String title){
        this.title = title;
    }
}
