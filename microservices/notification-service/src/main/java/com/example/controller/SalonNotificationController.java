package com.example.controller;

import com.example.mapper.NotificationMapper;
import com.example.model.Notification;
import com.example.payload.dto.BookingDto;
import com.example.payload.dto.NotificationDto;
import com.example.service.NotificationService;
import com.example.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/salon-owner")
@RequiredArgsConstructor
public class SalonNotificationController {
    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody Notification notification
    ) {
        return new ResponseEntity<>(notificationService.createNotification(notification), HttpStatus.OK);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<List<NotificationDto>> getNotificationBySalonId(
            @PathVariable Long salonId
    ) {
        List<Notification> notifications = notificationService.getAllNotificationsBySalonId(salonId);
        List<NotificationDto> notificationDtoList = NotificationMapper.mapToNotificationDtoList(
                notifications,
                bookingFeignClient
        );
        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDto> markNotificationAsRead(
        @PathVariable Long notificationId
    ) {
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        BookingDto booking = bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
        return new ResponseEntity<>(
                NotificationMapper.mapToNotificationDto(notification, booking),
                HttpStatus.OK
        );
    }
}
