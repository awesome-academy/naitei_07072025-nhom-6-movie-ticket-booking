package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Notification;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.entity.enums.NotificationStatus;
import com.org.Movie_Ticket_Booking.repository.NotificationRepository;
import com.org.Movie_Ticket_Booking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final MessageSource messageSource;

    @Override
    public void saveNoti(User user, String code, Object[] args, String method, Locale locale) {
        String message = messageSource.getMessage(code, args, locale);
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .status(NotificationStatus.UNREAD)
                .sendTime(LocalDateTime.now())
                .method(method)
                .build();

        notificationRepository.save(notification);
    }
}
