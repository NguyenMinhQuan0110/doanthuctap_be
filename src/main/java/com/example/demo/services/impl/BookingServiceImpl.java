package com.example.demo.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.BookingRequest;
import com.example.demo.dtos.response.BookingResponse;
import com.example.demo.dtos.response.TimeSlotResponse;
import com.example.demo.entites.Booking;
import com.example.demo.entites.Complex;
import com.example.demo.entites.Pitch;
import com.example.demo.entites.PitchGroup;
import com.example.demo.entites.TimeSlot;
import com.example.demo.entites.User;
import com.example.demo.entites.enums.BookingStatus;
import com.example.demo.entites.enums.TargetType;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.PitchGroupRepository;
import com.example.demo.repositories.PitchRepository;
import com.example.demo.repositories.TimeSlotRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.BookingService;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    @Autowired
    private ComplexRepository complexRepository;
    
    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private PitchGroupRepository pitchGroupRepository;

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TimeSlot slot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        // ✅ Kiểm tra trùng lịch
        if (bookingRepository.existsByTargetTypeAndTargetIdAndTimeSlotAndBookingDate(
                request.getTargetType(), request.getTargetId(), slot, request.getBookingDate())) {
            throw new RuntimeException("This target already has a booking in this time slot on that date");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTimeSlot(slot);
        booking.setTargetType(request.getTargetType());
        booking.setTargetId(request.getTargetId());
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(BookingStatus.pending);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse updateBooking(Integer id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        TimeSlot slot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        booking.setTargetType(request.getTargetType());
        booking.setTargetId(request.getTargetId());
        booking.setBookingDate(request.getBookingDate());
        booking.setTimeSlot(slot);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getBookingId());
        response.setTargetType(booking.getTargetType());
        response.setTargetId(booking.getTargetId());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());

        response.setUserId(booking.getUser().getUserId());
        response.setUserName(booking.getUser().getFullName());
        response.setTimeSlotId(booking.getTimeSlot().getTimeSlotId());
        response.setTimeSlotRange(booking.getTimeSlot().getStartTime() + " - " + booking.getTimeSlot().getEndTime());

        // Thêm thông tin targetName và complexAddress
        if (booking.getTargetType() == TargetType.pitch) {
            Pitch pitch = pitchRepository.findById(booking.getTargetId())
                    .orElseThrow(() -> new RuntimeException("Pitch not found"));
            response.setTargetName(pitch.getName());
            response.setComplexName(pitch.getComplex().getName());
        } else {
            PitchGroup group = pitchGroupRepository.findById(booking.getTargetId())
                    .orElseThrow(() -> new RuntimeException("PitchGroup not found"));
            response.setTargetName(group.getName());
            response.setComplexName(group.getComplex().getName());
        }

        return response;
    }
    @Override
    public List<BookingResponse> getBookingsByComplex(Integer complexId) {
        return bookingRepository.findByComplexId(complexId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Override
    public List<TimeSlotResponse> getAvailableTimeSlots(Integer complexId, TargetType targetType, Integer targetId, LocalDate bookingDate) {
        Complex complex = complexRepository.findById(complexId)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        // Lấy tất cả TimeSlot thuộc complex
        List<TimeSlot> allSlots = timeSlotRepository.findByComplex(complex);

        // Lấy danh sách booking trong ngày đó
        List<Booking> allBookings = bookingRepository.findByBookingDate(bookingDate);

        // Danh sách slotId bị chiếm
        Set<Integer> bookedSlotIds = new HashSet<>();

        if (targetType == TargetType.group) {
            // Nếu đang kiểm tra PitchGroup
            PitchGroup group = pitchGroupRepository.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("PitchGroup not found"));

            // 1️⃣: Lấy các Pitch con
            Set<Integer> pitchIds = group.getPitches().stream()
                    .map(Pitch::getPitchId)
                    .collect(Collectors.toSet());

            // 2️⃣: Nếu Group hoặc bất kỳ Pitch con nào đã được đặt ở timeslot nào → slot đó bị chặn
            for (Booking b : allBookings) {
                if (
                    // Bị chặn do Group này
                    (b.getTargetType() == TargetType.group && b.getTargetId().equals(group.getGroupId())) ||
                    // Bị chặn do một trong các Pitch con
                    (b.getTargetType() == TargetType.pitch && pitchIds.contains(b.getTargetId()))
                ) {
                    bookedSlotIds.add(b.getTimeSlot().getTimeSlotId());
                }
            }
        } else if (targetType == TargetType.pitch) {
            // Nếu đang kiểm tra Pitch riêng lẻ
            Pitch pitch = pitchRepository.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("Pitch not found"));

            // 1️⃣: Lấy tất cả Group chứa Pitch này
            List<PitchGroup> containingGroups = pitchGroupRepository.findAll().stream()
                    .filter(g -> g.getPitches().stream()
                            .anyMatch(p -> p.getPitchId().equals(pitch.getPitchId())))
                    .collect(Collectors.toList());

            Set<Integer> groupIds = containingGroups.stream()
                    .map(PitchGroup::getGroupId)
                    .collect(Collectors.toSet());

            // 2️⃣: Nếu Pitch này hoặc Group chứa nó đã được đặt ở timeslot nào → slot đó bị chặn
            for (Booking b : allBookings) {
                if (
                    (b.getTargetType() == TargetType.pitch && b.getTargetId().equals(pitch.getPitchId())) ||
                    (b.getTargetType() == TargetType.group && groupIds.contains(b.getTargetId()))
                ) {
                    bookedSlotIds.add(b.getTimeSlot().getTimeSlotId());
                }
            }
        }

        // 3️⃣: Lọc ra các slot chưa bị chiếm
        return allSlots.stream()
                .filter(slot -> slot.getStatus() == com.example.demo.entites.enums.TimeSlotStatus.active) // ✅ chỉ lấy slot active
                .filter(slot -> !bookedSlotIds.contains(slot.getTimeSlotId())) // ✅ loại slot đã bị đặt
                .map(this::mapToTimeSlotResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BookingResponse cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() == BookingStatus.cancelled) {
            throw new RuntimeException("This booking has already been cancelled");
        }
        
        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(LocalDateTime.now());
        
        // Nếu có payment thì có thể cập nhật payment luôn
        if (booking.getPayment() != null) {
            booking.getPayment().setStatus(com.example.demo.entites.enums.PaymentStatus.refunded);
            booking.getPayment().setPaidAt(LocalDateTime.now());
        }

        return mapToResponse(bookingRepository.save(booking));
    }

    
    private TimeSlotResponse mapToTimeSlotResponse(TimeSlot timeSlot) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setId(timeSlot.getTimeSlotId());
        response.setStartTime(timeSlot.getStartTime());
        response.setEndTime(timeSlot.getEndTime());
        response.setPrice(timeSlot.getPrice());
        response.setStatus(timeSlot.getStatus());
        response.setComplexId(timeSlot.getComplex().getComplexId());
        response.setComplexName(timeSlot.getComplex().getName());
        return response;
    }
}
