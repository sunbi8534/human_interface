package com.human.face;

import com.human.face.Dto.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;

@org.springframework.stereotype.Service
public class Service {
    Repository repository;
    public Service(Repository repository) {
        this.repository = repository;
    }

    public NewExperimentResponse getNewExperimentNum() {
        int experimenter_num = repository.getNewExperimentNum();
        boolean meaw_first = true;
        if (experimenter_num % 2 == 0)
            meaw_first = false;
        repository.createMoveTable(experimenter_num);

        return new NewExperimentResponse(experimenter_num, meaw_first);
    }

    public void saveTarget(int experimentId, String type, TargetRequest targetRequest) {
        repository.saveTarget(experimentId, type, targetRequest);
    }

    public void saveUserMove(int experimentId, String type, int sequence, UserMoveRequest userMoveRequest) {
        if (userMoveRequest.isRay_reached() && userMoveRequest.isRay_selected())
            repository.saveTargetDone(experimentId, type, sequence, userMoveRequest);
        else
            repository.saveUserMove(experimentId, type, sequence, userMoveRequest);
    }

    public void export(int experimentId) {
        List<UserPosition> userPositions = repository.getUserPosition(experimentId);
        String filename = experimentId + "_user_moves.xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("User Moves");

        // 3. 헤더 생성
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Type");
        header.createCell(1).setCellValue("Sequence");
        header.createCell(2).setCellValue("position_x");
        header.createCell(3).setCellValue("position_y");
        header.createCell(4).setCellValue("position_z");
        header.createCell(5).setCellValue("time_ms");
        header.createCell(6).setCellValue("ray_x");
        header.createCell(7).setCellValue("ray_y");
        header.createCell(8).setCellValue("ray_z");
        header.createCell(9).setCellValue("ray reached");
        header.createCell(10).setCellValue("ray_selected");
        header.createCell(11).setCellValue("time_ms");

        int rowNum = 1;
        Row row;
        for(UserPosition up : userPositions) {
            row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(up.getType());
            row.createCell(1).setCellValue(up.getSeq());
            row.createCell(2).setCellValue(up.getPosition_x());
            row.createCell(3).setCellValue(up.getPosition_y());
            row.createCell(4).setCellValue(up.getPosition_z());
            row.createCell(5).setCellValue(up.getTime_ms());

            List<UserMove> userMoves = repository.getUserMoves(experimentId, up.getType(), up.getSeq());
            for(UserMove mv : userMoves) {
                row.createCell(6).setCellValue(mv.getRay_x());
                row.createCell(7).setCellValue(mv.getRay_y());
                row.createCell(8).setCellValue(mv.getRay_z());
                row.createCell(9).setCellValue(mv.isRay_reached());
                row.createCell(10).setCellValue(mv.isRay_selected());
                row.createCell(11).setCellValue(mv.getTime_ms());
                row = sheet.createRow(++rowNum);
            }
            List<UserMove> dones = repository.getUserDoneMoves(experimentId, up.getType(), up.getSeq());
            if(!dones.isEmpty()) {
                UserMove don = dones.get(0);
                row.createCell(6).setCellValue(don.getRay_x());
                row.createCell(7).setCellValue(don.getRay_y());
                row.createCell(8).setCellValue(don.getRay_z());
                row.createCell(9).setCellValue(don.isRay_reached());
                row.createCell(10).setCellValue(don.isRay_selected());
                row.createCell(11).setCellValue(don.getTime_ms());
            }
            rowNum++;
        }

        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
            System.out.println("Excel file created: user_moves.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
