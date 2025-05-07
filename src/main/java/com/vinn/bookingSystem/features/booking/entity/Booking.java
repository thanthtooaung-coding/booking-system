package com.vinn.bookingSystem.features.booking.entity;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.user.entity.User;
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
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ClassSchedule schedule;

    @Column(nullable = false)
    private Integer creditsUsed;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    private Integer status;

    @Column(nullable = false)
    private boolean checkedIn;

    private LocalDateTime checkInTime;

    private LocalDateTime cancellationTime;

    @Column(nullable = false)
    private boolean creditRefunded = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
