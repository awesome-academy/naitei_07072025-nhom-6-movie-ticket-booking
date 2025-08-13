package com.org.Movie_Ticket_Booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Number_phone")
    private String numberPhone;

    @Column(name = "Birth_day")
    private LocalDate birthDay;

    @Column(name = "Gender")
    private Integer gender;

    @Column(name = "Password")
    private String password;

    @Column(name = "Is_verified")
    private Integer isVerified;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Roles",
            joinColumns = @JoinColumn(name = "User_ID"),
            inverseJoinColumns = @JoinColumn(name = "Role_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Lấy danh sách quyền từ các role của user
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Sử dụng email làm username để đăng nhập
    }

    @Override
    public boolean isEnabled() {
        return this.isVerified != null && this.isVerified == 1;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}