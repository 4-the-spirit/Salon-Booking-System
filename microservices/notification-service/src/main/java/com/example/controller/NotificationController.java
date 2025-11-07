package com.example.controller;

import com.example.mapper.NotificationMapper;
import com.example.model.Notification;
import com.example.payload.dto.NotificationDto;
import com.example.service.NotificationService;
import com.example.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody Notification notification
    ) {
        return new ResponseEntity<>(
                notificationService.createNotification(notification),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotificationByUserId(
            @PathVariable Long userId
    ) {
        List<Notification> notifications = notificationService.getAllNotificationsByUserId(userId);
        List<NotificationDto> notificationDtoList = NotificationMapper.mapToNotificationDtoList(
                notifications,
                bookingFeignClient
        );
        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }
}
