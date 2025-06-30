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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.OutputStream;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class UserController {

    @GetMapping("/")
    public String home(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://reqres.in/api/users?page=1";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        List<User> users = new ArrayList<>();
        for(Map<String, Object> userData : data){
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

}
