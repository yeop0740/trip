package com.example.trip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성일자와 수정일자는 모든 테이블이 공통으로 가지고 있는 정보
 *
 * 추상 클래스로 만들어 상속해서 사용
 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "created_time")
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(name = "modified_time")
    @LastModifiedDate
    private LocalDateTime modifiedTime;

    public void setModifiedTime(){
        this.modifiedTime = LocalDateTime.now();
    }
}
