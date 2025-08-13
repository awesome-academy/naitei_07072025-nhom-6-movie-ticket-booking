package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    @Column(length = 255)
    private String address;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
    @Column(name = "number_phone", length = 10)
    private String numberPhone;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Min(value = 0, message = "Giới tính không hợp lệ")
    @Max(value = 1, message = "Giới tính không hợp lệ")
    private Integer gender;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải chứa ít nhất 8 ký tự")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @Column(name = "verification_expiry")
    private LocalDateTime verificationExpiry;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Cinema> cinemas;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Contact> contacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PromotionUser> promotionUsers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UpgradeRequest> upgradeRequests;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}

