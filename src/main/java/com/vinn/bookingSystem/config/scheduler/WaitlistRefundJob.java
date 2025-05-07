package com.vinn.bookingSystem.config.scheduler;

import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import com.vinn.bookingSystem.features.userpackages.repository.UserPackageRepository;
import com.vinn.bookingSystem.features.waitlist.entity.Waitlist;
import com.vinn.bookingSystem.features.waitlist.entity.WaitlistStatus;
import com.vinn.bookingSystem.features.waitlist.repository.WaitlistRepository;
import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class WaitlistRefundJob implements Job {

    private final WaitlistRepository waitlistRepository;
    private final UserPackageRepository userPackageRepository;

    @Override
    public void execute(JobExecutionContext context) {
        List<Waitlist> toRefund = waitlistRepository.findAllBySchedule_EndTimeBeforeAndStatus(
            LocalDateTime.now(), WaitlistStatus.WAITING.getValue());

        for (Waitlist w : toRefund) {
            UserPackage pkg = w.getUserPackage();
            pkg.setRemainingCredits(pkg.getRemainingCredits() + w.getCreditsReserved());
            userPackageRepository.save(pkg);

            w.setStatus(WaitlistStatus.CANCELLED.getValue());
            waitlistRepository.save(w);
        }
    }
}
