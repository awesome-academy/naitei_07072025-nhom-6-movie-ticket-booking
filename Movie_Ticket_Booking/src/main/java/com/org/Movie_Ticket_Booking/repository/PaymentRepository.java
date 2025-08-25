package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Payment;
import org.springframework.data.repository.ListCrudRepository;

public interface PaymentRepository extends ListCrudRepository<Payment, Long> {
}
