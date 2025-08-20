package com.org.Movie_Ticket_Booking.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class FileReader {
    /*Read file .xlsx*/
    public List<List<String>> readExcel(File file){
        List<List<String>> data = new ArrayList<>();
        try(Workbook workbook = WorkbookFactory.create(file)){
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            DataFormatter formatter = new DataFormatter();

            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                List<String> rowData = new ArrayList<>();

                for(Cell cell : row){
                    rowData.add(formatter.formatCellValue(cell));
                }
                boolean emptyRow = rowData.stream().allMatch(s -> s == null || s.isBlank());
                if (!emptyRow) {
                    data.add(rowData);
                }
            }
        }catch (IOException e){
            throw new AppException(ErrorCode.FILE_ERROR);
        }
        return data;
    }

    /*Read file csv*/
    public List<List<String>> readCSV(File file){
        try(CSVReader csvReader = new CSVReader(new java.io.FileReader(file))){
            List<String[]> rows = csvReader.readAll();
            List<List<String>> data = new ArrayList<>();

            for(String[] row : rows){
                boolean emptyRow = Arrays.stream(row).allMatch(s -> s == null || s.isBlank());
                if (!emptyRow) {
                    data.add(Arrays.asList(row));
                }
            }
            return data;
        }catch (IOException | CsvException e){
            throw new AppException(ErrorCode.FILE_ERROR);
        }
    }
    public List<List<String>> readFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (fileName == null) throw new AppException(ErrorCode.FILE_ERROR);

        File tempFile = File.createTempFile("upload-", fileName);
        file.transferTo(tempFile);

        List<List<String>> rows;

        if ((fileName.endsWith(".csv") && (contentType == null || contentType.equals("text/csv")))) {
            rows = readCSV(tempFile);
        } else if (fileName.endsWith(".xlsx") &&
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            rows = readExcel(tempFile);
        } else {
            tempFile.delete();
            throw new AppException(ErrorCode.FILE_INVALID);
        }

        boolean deleted = deleteTempFile(tempFile);
        if (!deleted) {
            throw new AppException(ErrorCode.UNIDENTIFIED_ERROR);
        }
        return rows;
    }

    private boolean deleteTempFile(File tempFile) {
        if (tempFile == null || !tempFile.exists()) {
            return true;
        }
        return tempFile.delete();
    }
}
