package com.example.ideas_generator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(String idea, String development, String level, String conversation) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Idee de Proiect Java pentru Juniori", titleFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Idea Originală:", subtitleFont));
            document.add(new Paragraph(idea, bodyFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Nivel de Dezvoltare:", subtitleFont));
            document.add(new Paragraph(level, bodyFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Dezvoltarea Ideii:", subtitleFont));
            document.add(new Paragraph(development, bodyFont));
            document.add(Chunk.NEWLINE);

            if (!conversation.isEmpty()) {
                document.add(new Paragraph("Conversație Continuată:", subtitleFont));
                document.add(new Paragraph(conversation, bodyFont));
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return out.toByteArray();
    }
}