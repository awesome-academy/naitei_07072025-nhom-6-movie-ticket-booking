package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.repository.RoomRepository;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    private CinemaRepository cinemaRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, CinemaRepository cinemaRepository){
        this.roomRepository=roomRepository;
        this.cinemaRepository=cinemaRepository;
    }

    @Override
    public List<Room> findRoomByCinemaId(Long id) {
        return this.roomRepository.findByCinemaId(id);
    }

    @Override
    public List<Room> findAll() {
        return this.roomRepository.findAll();
    }

    @Override
    public Optional<Room> findById(Long id) {
        return this.roomRepository.findById(id);
    }

    @Override
    public void saveExcelData(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ dòng header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = row.getCell(0).getStringCellValue();
                int quantitySeat = (int) row.getCell(1).getNumericCellValue();

                Long cinema_id = (long) row.getCell(2).getNumericCellValue();
                Cinema cinema = cinemaRepository.findById(cinema_id).orElseThrow(()-> new AppException(ErrorCode.UNIDENTIFIED_ERROR));
                Room room = Room.builder().name(name)
                        .quantitySeats(quantitySeat)
                        .cinema(cinema)
                        .build();
                this.roomRepository.save(room);
            }
        }
    }
}
