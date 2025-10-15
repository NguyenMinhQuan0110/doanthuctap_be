package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entites.Booking;
import com.example.demo.entites.TimeSlot;
import com.example.demo.entites.User;
import com.example.demo.entites.enums.TargetType;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
    List<Booking> findByUser(User user);
    List<Booking> findByTimeSlot(TimeSlot timeSlot);
    List<Booking> findByTargetTypeAndTargetId(TargetType type, Integer targetId);
    boolean existsByTargetTypeAndTargetIdAndTimeSlotAndBookingDate(TargetType type, Integer targetId, TimeSlot slot, LocalDate date);
    @Query("SELECT b FROM Booking b WHERE b.timeSlot.complex.complexId = :complexId")
    List<Booking> findByComplexId(@Param("complexId") Integer complexId);
    List<Booking> findByBookingDate(LocalDate bookingDate);
    @Query(
    		  value = "SELECT group_id FROM pitchgroupdetail WHERE pitch_id = :pitchId",
    		  nativeQuery = true
    		)
    		List<Integer> findGroupIdsByPitchId(@Param("pitchId") Integer pitchId);

    		@Query(
    		  value = "SELECT pitch_id FROM pitchgroupdetail WHERE group_id = :groupId",
    		  nativeQuery = true
    		)
    		List<Integer> findPitchIdsByGroupId(@Param("groupId") Integer groupId);
}
