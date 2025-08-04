package com.example.controller;

import com.example.domain.BookingStatus;
import com.example.mapper.BookingMapper;
import com.example.model.Booking;
import com.example.payload.dto.*;
import com.example.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestParam Long salonId,
            @RequestBody BookingRequestDto bookingRequestDto) {

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        SalonDto salonDto = new SalonDto();
        salonDto.setId(salonId);
        salonDto.setOpenTime(LocalTime.of(0, 0));
        salonDto.setCloseTime(LocalTime.of(23, 0));

        Set<ServiceOfferingDto> serviceOfferingDtoSet = new HashSet<>();

        ServiceOfferingDto serviceOfferingDto = new ServiceOfferingDto();
        serviceOfferingDto.setId(1L);
        serviceOfferingDto.setPrice(399D);
        serviceOfferingDto.setDuration(45);
        serviceOfferingDto.setName("Hair cut for men");

        serviceOfferingDtoSet.add(serviceOfferingDto);

        Booking createdBooking = bookingService.createBooking(
                bookingRequestDto,
                userDto,
                salonDto,
                serviceOfferingDtoSet);

        return new ResponseEntity<>(BookingMapper.mapToBookingDto(createdBooking), HttpStatus.OK);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDto>> getBookingsByCustomer() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Set<Booking> bookingSet = bookingService.getBookingsByCustomer(1L);
        return new ResponseEntity<>(BookingMapper.mapToBookingDtoSet(bookingSet), HttpStatus.OK);
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDto>> getBookingsBySalon() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Set<Booking> bookingSet = bookingService.getBookingsBySalon(1L);
        return new ResponseEntity<>(BookingMapper.mapToBookingDtoSet(bookingSet), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(
            @PathVariable("bookingId") Long bookingId
    ) {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Booking booking = bookingService.getBookingById(bookingId);
        return new ResponseEntity<>(BookingMapper.mapToBookingDto(booking), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingDto> updateBookingStatus(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("status") BookingStatus status
            ) {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return new ResponseEntity<>(BookingMapper.mapToBookingDto(updatedBooking), HttpStatus.OK);
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<Set<BookingSlotDto>> getBookedSlots(
            @PathVariable("salonId") Long salonId,
            @PathVariable("date") LocalDate date
    ) {
        Set<Booking> bookings = bookingService.getBookingsByDate(date, salonId);
        Set<BookingSlotDto> slotDtoSet = bookings.stream()
                .map(booking -> {
                    BookingSlotDto slot = new BookingSlotDto();
                    slot.setStartTime(booking.getStartTime());
                    slot.setEndTime(booking.getEndTime());
                    return slot;
                })
                .collect(Collectors.toSet());

        return new ResponseEntity<>(slotDtoSet, HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReportDto> getSalonReport() {
        SalonReportDto report = bookingService.getSalonReport(1L);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }


}
