package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.Habitacion;
import com.example.demo.model.Reserva;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import jakarta.annotation.PostConstruct;

@Service
public class EmailService {
    
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @PostConstruct
    public void init() {
        System.out.println("SendGrid API Key: " + sendGridApiKey);
    }

    public void enviarCorreo(Reserva reserva, String destinatario, String nombreDestinatario, Habitacion habitacion,
            LocalDate fechaReserva, LocalDate fechaFinReserva) throws IOException {
        Email from = new Email("caterinmorales111@gmail.com");
        Email to = new Email(destinatario);

        String asunto = "Estas a un clic de reservar tu habitacion";
        String contenido = this.correoHtml(reserva, habitacion, fechaReserva, fechaFinReserva, nombreDestinatario);

        Content emailContent = new Content("text/html", contenido);
        Mail mail = new Mail(from, asunto, to, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

            System.out.println("Se ha enviado el correo de confirmacion a : " + destinatario);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public String correoHtml(Reserva reserva, Habitacion habitacion, LocalDate fechaReserva, LocalDate fechaFinReserva,
            String nombreDestinatario) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));

        String contenidoHTML = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }"
                +
                ".header { background-color: #783200; color: white; padding: 10px; text-align: center; border-radius: 10px 10px 0 0; }"
                +
                ".content { padding: 20px; }" +
                ".button { display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #783200; text-decoration: none; border-radius: 5px; margin-top: 20px; }"
                +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h2>Confirmación de Reserva</h2></div>" +
                "<div class='content'>" +
                "<p>" + nombreDestinatario.toUpperCase() + " gracias por tu reserva. Aquí tienes los detalles:</p>" +
                "<p><strong>Habitación:</strong> " + habitacion.getNombre() + "</p>" +
                "<p><strong>Fecha de inicio:</strong> " + fechaReserva.format(formatter) + "</p>" +
                "<p><strong>Fecha de termino:</strong> " + fechaFinReserva.format(formatter) + "</p>" +
                "<a href='http://hotel-back-production.up.railway.app/reserva/confirmar?reservaId=" + reserva.getId()
                + "' class='button' style='color:#fff'>Confirmar Reserva</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return contenidoHTML;
    }

}
