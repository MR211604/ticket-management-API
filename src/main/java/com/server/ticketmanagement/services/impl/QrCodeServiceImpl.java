package com.server.ticketmanagement.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.server.ticketmanagement.domain.entities.QRCode;
import com.server.ticketmanagement.domain.entities.QRCodeStatus;
import com.server.ticketmanagement.domain.entities.Ticket;
import com.server.ticketmanagement.exceptions.QrCodeGenerationException;
import com.server.ticketmanagement.repositories.QrCodeRepository;
import com.server.ticketmanagement.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {


    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;


    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QRCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage(uniqueId);

            QRCode qrCode = new QRCode();
            qrCode.setId(uniqueId);
            qrCode.setStatus(QRCodeStatus.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);

            return qrCodeRepository.saveAndFlush(qrCode);

        } catch (WriterException | IOException ex) {
            throw new QrCodeGenerationException("Failed to generate QR code", ex);
        }

    }

    public String generateQrCodeImage(UUID id) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                id.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);

            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);

        }


    }
}
