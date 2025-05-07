package com.vinn.bookingSystem.features.waitlist.entity;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "waitlist")
public class Waitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ClassSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_package_id", nullable = false)
    private UserPackage userPackage;

    @Column(nullable = false)
    private LocalDateTime addedTime;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Integer creditsReserved;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
