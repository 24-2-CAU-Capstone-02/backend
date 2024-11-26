package com.capstone.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String googleId;

    private LocalDate lastRoomCreatedDate;

    @Column(nullable = false)
    private Integer dailyRoomCount;

    public void incrementDailyRoomCount() {
        this.dailyRoomCount++;
    }

    public void resetDailyRoomCount() {
        this.dailyRoomCount = 0;
    }
}
