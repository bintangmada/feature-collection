package com.bintang.controller;

import com.bintang.model.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.OnClose;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class UserController {

    @GetMapping("/")
    public String home(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://reqres.in/api/users?page=1";

        // Tambahkan header x-api-key
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "reqres-free-v1");

        // Bungkus dengan HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Kirim GET request dengan header
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        List<User> users = new ArrayList<>();
        for (Map<String, Object> userData : data) {
            User user = new User();
            user.setId((int) userData.get("id"));
            user.setEmail((String) userData.get("email"));
            user.setFirstName((String) userData.get("first_name"));
            user.setLastName((String) userData.get("last_name"));
            user.setAvatar((String) userData.get("avatar"));
            users.add(user);
        }

        model.addAttribute("users", users);
        return "table";
    }


    @GetMapping("/export")
    public void exportPdf(HttpServletResponse response) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://reqres.in/api/users?page=1";
        ResponseEntity<Map> apiResponse = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) apiResponse.getBody().get("data");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        OutputStream out = response.getOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        PdfWriter.getInstance(document, out);

        document.open();
        Paragraph title = new Paragraph("User Data Table", new Font(Font.HELVETICA, 16, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 3f, 4f, 3f});

        // Header
        Stream.of("ID", "Full Name", "Email", "Avatar").forEach(header -> {
            PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE)));
            cell.setBackgroundColor(Color.BLACK);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        });

        for (Map<String, Object> userData : data) {
            PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(userData.get("id"))));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idCell.setFixedHeight(60f);
            table.addCell(idCell);

            PdfPCell nameCell = new PdfPCell(new Phrase(userData.get("first_name") + " " + userData.get("last_name")));
            nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nameCell.setFixedHeight(60f);
            table.addCell(nameCell);

            PdfPCell emailCell = new PdfPCell(new Phrase((String) userData.get("email")));
            emailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emailCell.setFixedHeight(60f);
            table.addCell(emailCell);

            try {
                Image avatarImage = Image.getInstance((String) userData.get("avatar"));
                avatarImage.scaleAbsolute(50, 50);
                PdfPCell imageCell = new PdfPCell(avatarImage, true);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                imageCell.setFixedHeight(60f);
                table.addCell(imageCell);
            } catch (Exception e) {
                PdfPCell errorCell = new PdfPCell(new Phrase("Image error"));
                errorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                errorCell.setFixedHeight(60f);
                table.addCell(errorCell);
            }
        }


        document.add(table);
        document.close();
    }

    @GetMapping("/export-excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://reqres.in/api/users?page=1";
        ResponseEntity<Map> apiResponse = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) apiResponse.getBody().get("data");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Full Name");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Avatar");

        int rowNum = 1;
        for (Map<String, Object> user : data) {
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(60); // tinggi baris untuk gambar

            row.createCell(0).setCellValue((Integer) user.get("id"));
            row.createCell(1).setCellValue(user.get("first_name") + " " + user.get("last_name"));
            row.createCell(2).setCellValue((String) user.get("email"));

            // Download dan tambahkan gambar avatar
            String avatarUrl = (String) user.get("avatar");
            try (InputStream inputStream = new URL(avatarUrl).openStream()) {
                byte[] bytes = inputStream.readAllBytes();
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                CreationHelper helper = workbook.getCreationHelper();
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(3);
                anchor.setRow1(rowNum);
                anchor.setCol2(4);
                anchor.setRow2(rowNum + 1);
                drawing.createPicture(anchor, pictureIdx);
            } catch (IOException e) {
                row.createCell(3).setCellValue("Image load failed");
            }

            rowNum++;
        }

        // Auto-size kolom kecuali kolom gambar
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
        sheet.setColumnWidth(3, 5000); // Set lebar kolom avatar manual (px)

        workbook.write(response.getOutputStream());
        workbook.close();
    }



}
