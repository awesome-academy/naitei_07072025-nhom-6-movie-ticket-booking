package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "type_seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên loại ghế không được để trống")
    @Column(length = 50, nullable = false)
    private String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "Phụ phí phải >= 0")
    @Column(name = "extra_price", precision = 10, scale = 2)
    private BigDecimal extraPrice;

    @OneToMany(mappedBy = "typeSeat", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Seat> seats;
}

