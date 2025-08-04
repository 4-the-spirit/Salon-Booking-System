package com.example.controller;

import com.example.domain.PaymentMethod;
import com.example.model.PaymentOrder;
import com.example.payload.dto.BookingDto;
import com.example.payload.dto.UserDto;
import com.example.payload.response.PaymentLinkResponse;
import com.example.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDto booking,
            @RequestParam PaymentMethod paymentMethod
            ) throws StripeException {
        UserDto userDto = new UserDto();
        userDto.setFullName("Dana");
        userDto.setId(1L);

        PaymentLinkResponse paymentLinkResponse = paymentService.createOrder(
                userDto,
                booking,
                paymentMethod);

        return new ResponseEntity<>(paymentLinkResponse, HttpStatus.OK);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable("paymentOrderId") Long paymentOrderId
    ) {
        PaymentOrder order = paymentService.getPaymentOrderById(paymentOrderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/proceed")
    public ResponseEntity<PaymentOrder> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId
    ) {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        paymentService.proceedPaymentStatus(paymentOrder, paymentId, paymentLinkId);
        return new ResponseEntity<>(paymentOrder, HttpStatus.OK);
    }

}
