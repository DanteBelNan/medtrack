package com.medtrack.repository;

import com.medtrack.model.MedicineLog;
import com.medtrack.model.LogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicineLogRepository extends JpaRepository<MedicineLog, Long> {

    List<MedicineLog> findByUserId(Long userId);
    List<MedicineLog> findByMedicineId(Long medicineId);


    @Query("SELECT l FROM MedicineLog l WHERE l.status = 'PENDING' " +
            "AND l.scheduledTime BETWEEN :start AND :end " +
            "AND l.notificationSentAt IS NULL")
    List<MedicineLog> findPendingNotifications(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);
}