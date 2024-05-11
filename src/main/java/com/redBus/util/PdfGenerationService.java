package com.redBus.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.redBus.user.payload.BookingDetailsDto;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(BookingDetailsDto bookingDetails) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add booking details to the PDF
        addBookingDetailsToPdf(document, bookingDetails);

        document.close();
        return outputStream.toByteArray();
    }

    private void addBookingDetailsToPdf(Document document, BookingDetailsDto bookingDetails) {
        // Add details to the PDF using iText
        document.add(new Paragraph("Booking ID: " + bookingDetails.getBookingId()));
        document.add(new Paragraph("Bus Company: " + bookingDetails.getBusCompany()));
        document.add(new Paragraph("From City: " + bookingDetails.getFromCity()));
        document.add(new Paragraph("To City: " + bookingDetails.getToCity()));
        document.add(new Paragraph("Departure Date: " + bookingDetails.getDepartureDate().toString()));
        document.add(new Paragraph("Departure Time: " + bookingDetails.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        document.add(new Paragraph("Arrival Date: " + bookingDetails.getArrivalDate().toString()));
        document.add(new Paragraph("Arrival Time: " + bookingDetails.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        document.add(new Paragraph("Total Travel Time: " + bookingDetails.getTotalTravelTime()));
        document.add(new Paragraph("First Name: " + bookingDetails.getFirstName()));
        document.add(new Paragraph("Last Name: " + bookingDetails.getLastName()));
        document.add(new Paragraph("Email: " + bookingDetails.getEmail()));
        document.add(new Paragraph("Mobile: " + bookingDetails.getMobile()));
        document.add(new Paragraph("Price: " + bookingDetails.getPrice()));
    }



    public byte[] generatePdfForCancellation(BookingDetailsDto cancellationDetails) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add cancellation details to the PDF
        addCancellationDetailsToPdf(document, cancellationDetails);

        document.close();
        return outputStream.toByteArray();
    }

    private void addCancellationDetailsToPdf(Document document, BookingDetailsDto cancellationDetails) {
        // Add cancellation details to the PDF using iText
        document.add(new Paragraph("Booking ID: " + cancellationDetails.getBookingId()));
        document.add(new Paragraph("Bus Company:"+cancellationDetails.getBusCompany()));
        document.add(new Paragraph("From City: " + cancellationDetails.getFromCity()));
        document.add(new Paragraph("To City: " + cancellationDetails.getToCity()));
        document.add(new Paragraph("Departure Time: " + cancellationDetails.getDepartureTime()));
        document.add(new Paragraph("Arrival Time: " + cancellationDetails.getArrivalTime()));
        document.add(new Paragraph("Departure Date: " + cancellationDetails.getDepartureDate()));
        document.add(new Paragraph("Arrival Date: " + cancellationDetails.getArrivalDate()));
        document.add(new Paragraph("Passenger Name: " + cancellationDetails.getFirstName() + " " + cancellationDetails.getLastName()));
        document.add(new Paragraph("Email: " + cancellationDetails.getEmail()));
        document.add(new Paragraph("Mobile: " + cancellationDetails.getMobile()));
        document.add(new Paragraph("Price:"+ cancellationDetails.getPrice()));

    }
}




