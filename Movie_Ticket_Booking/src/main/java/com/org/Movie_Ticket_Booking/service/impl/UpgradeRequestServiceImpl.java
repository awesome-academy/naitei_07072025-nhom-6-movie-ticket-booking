package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.UpgradeRqDTO;
import com.org.Movie_Ticket_Booking.dto.respone.UpgradeRqResponse;
import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Role;
import com.org.Movie_Ticket_Booking.entity.UpgradeRequest;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.entity.enums.UpgradeRequestStatus;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.mapper.UpgradeRequestMapper;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.repository.RoleRepository;
import com.org.Movie_Ticket_Booking.repository.UpgradeRequestRepository;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.service.NotificationService;
import com.org.Movie_Ticket_Booking.service.UpgradeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpgradeRequestServiceImpl implements UpgradeRequestService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UpgradeRequestRepository upgradeRequestRepository;
    private final CinemaRepository cinemaRepository;
    private final UpgradeRequestMapper upgradeRequestMapper;
    private final NotificationService notificationService;
    private final String MANAGER ="ROLE_CINEMA_MANAGER";
    private final String METHOD_SYSTEM = "SYSTEM";

    @Override
    public UpgradeRqResponse createUpgradeRequest(UpgradeRqDTO upgradeRqDTO) {
        User user = getUser(upgradeRqDTO.getUserId());
        checkDuplicateCinemaGlobal(upgradeRqDTO);
        checkUserExistingRequest(upgradeRqDTO);
        UpgradeRequest upgradeRequest = saveUpgradeRequest(upgradeRqDTO, user);
        return upgradeRequestMapper.toClientResponse(upgradeRequest);
    }

    @Override
    public Page<UpgradeRqResponse> getAllRequest(Pageable pageable) {
        return upgradeRequestRepository.findAllRequest(pageable);
    }

    @Override
    public Page<UpgradeRqResponse> getRequestsByStatus(String status, Pageable pageable) {
        UpgradeRequestStatus enumStatus = null;
        if (status !=  null && !status.isBlank()){
            boolean isValid = Arrays.stream(UpgradeRequestStatus.values())
                    .anyMatch(s -> s.name().equalsIgnoreCase(status));

            if (!isValid) {
                throw new AppException(ErrorCode.INVALID_STATUS);
            }
            enumStatus = UpgradeRequestStatus.valueOf(status.toUpperCase());
        }
        return upgradeRequestRepository.findAllRequest(enumStatus, pageable);
    }

    @Override
    public UpgradeRqResponse getDetailByID(Long id) {
        getRequest(id);
        return upgradeRequestRepository.findDetailById(id);
    }

    @Transactional
    @Override
    public void approve(Long id, Locale locale) {
        UpgradeRequest upgradeRequest = getRequest(id);

        if (upgradeRequest.getStatus() != UpgradeRequestStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        upgradeRequest.setStatus(UpgradeRequestStatus.APPROVED);

        User user = upgradeRequest.getUser();

        Role managerRole = roleRepository.findByName(MANAGER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.getRoles().add(managerRole);

        Cinema cinema = Cinema.builder()
                .name(upgradeRequest.getCinemaName())
                .address(upgradeRequest.getAddress())
                .description(upgradeRequest.getDescription())
                .manager(user)
                .build();

        cinemaRepository.save(cinema);
        upgradeRequestRepository.save(upgradeRequest);
        notificationService.saveNoti( user,
                "notification.upgrade.approved",
                new Object[] {upgradeRequest.getCinemaName()},
                METHOD_SYSTEM,
                locale);
    }

    @Transactional
    @Override
    public void reject(Long id, Locale locale) {
        UpgradeRequest upgradeRequest = getRequest(id);

        if (upgradeRequest.getStatus() != UpgradeRequestStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        upgradeRequest.setStatus(UpgradeRequestStatus.REJECTED);
        upgradeRequestRepository.save(upgradeRequest);

        notificationService.saveNoti(
                upgradeRequest.getUser(),
                "notification.upgrade.rejected",
                new Object[]{upgradeRequest.getCinemaName()},
                METHOD_SYSTEM,
                locale
        );
    }

    private User getUser(Long userID){
        return  userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private UpgradeRequest getRequest(Long requestID){
        return upgradeRequestRepository.findById(requestID)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
    }

    private void checkDuplicateCinemaGlobal(UpgradeRqDTO dto) {
        Optional<UpgradeRequest> existing = upgradeRequestRepository
                .findByCinemaNameAndAddress(dto.getCinemaName(), dto.getAddress());

        if (existing.isPresent()) {
            UpgradeRequest req = existing.get();
            if (!req.getUser().getId().equals(dto.getUserId()) &&
                    (req.getStatus() == UpgradeRequestStatus.PENDING
                    || req.getStatus() == UpgradeRequestStatus.APPROVED)) {
                throw new AppException(ErrorCode.UPGRADE_EXISTING);
            }
        }
    }

    private void checkUserExistingRequest(UpgradeRqDTO dto) {
        Optional<UpgradeRequest> userExisting = upgradeRequestRepository
                .findByUser_IdAndCinemaNameAndAddress(dto.getUserId(),
                        dto.getCinemaName(),
                        dto.getAddress());

        userExisting.ifPresent(req -> {
            switch (req.getStatus()) {
                case PENDING -> throw new AppException(ErrorCode.UPGRADE_EXISTING_PENDING);
                case APPROVED -> throw new AppException(ErrorCode.UPGRADE_APPROVED);
                case REJECTED -> {
                    // Allow resending requests
                }
            }
        });
    }

    private UpgradeRequest saveUpgradeRequest(UpgradeRqDTO upgradeRqDTO, User user){
        UpgradeRequest upgradeRequest = upgradeRequestMapper.toEntity(upgradeRqDTO);
        upgradeRequest.setUser(user);
        upgradeRequest.setStatus(UpgradeRequestStatus.PENDING);
        return upgradeRequestRepository.save(upgradeRequest);
    }
}
