package com.medtrack.scheduler;

import com.medtrack.dto.MedicineLogDTO;
import com.medtrack.mapper.MedicineLogMapper;
import com.medtrack.model.MedicineLog;
import com.medtrack.producer.NotificationProducer;
import com.medtrack.repository.MedicineLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationWorker {

    private final MedicineLogRepository logRepository;
    private final NotificationProducer notificationProducer;
    private final MedicineLogMapper logMapper;

    public NotificationWorker(MedicineLogRepository logRepository,
                              NotificationProducer notificationProducer,
                              MedicineLogMapper logMapper) {
        this.logRepository = logRepository;
        this.notificationProducer = notificationProducer;
        this.logMapper = logMapper;
    }

    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void processPendingNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<MedicineLog> pendingLogs = logRepository.findPendingNotifications(
                now.minusMinutes(5),
                now.plusMinutes(15)
        );

        for (MedicineLog log : pendingLogs) {
            MedicineLogDTO dto = logMapper.toDTO(log);
            notificationProducer.sendNotification(dto);

            log.setNotificationSentAt(LocalDateTime.now());
            logRepository.save(log);
        }
    }
}