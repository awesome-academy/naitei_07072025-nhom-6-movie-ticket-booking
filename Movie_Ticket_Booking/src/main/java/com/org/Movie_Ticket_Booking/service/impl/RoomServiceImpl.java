package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.repository.RoomRepository;
import com.org.Movie_Ticket_Booking.service.RoomService;
import com.org.Movie_Ticket_Booking.utils.FileReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public void saveData(MultipartFile file) throws IOException {
        FileReader fileReader = new FileReader();
        List<List<String>> data = fileReader.readFile(file);
        if(data==null){
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        for (List<String> row : data) {
            if (row.size() < 3) {
                throw new AppException(ErrorCode.FILE_ERROR);
            }
            String name = row.get(0);
            if (name == null || name.trim().isEmpty()) {
                throw new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            int quantitySeats;
            try {
                quantitySeats = Integer.parseInt(row.get(1));
                if (quantitySeats <= 0) throw new AppException(ErrorCode.DATA_NOT_FOUND);
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.WRONG_DATATYPE);
            }
            Long cinemaId;
            try {
                cinemaId = Long.parseLong(row.get(2));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.WRONG_DATATYPE);
            }

            Cinema cinema = cinemaRepository.findById(cinemaId)
                    .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

            Room room = Room.builder()
                    .name(name)
                    .quantitySeats(quantitySeats)
                    .cinema(cinema)
                    .build();

            roomRepository.save(room);
        }
    }

}
