package com.udi.app.framework.utilities.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by leonard on 5/4/2017.
 */

public class QRCodeUtils {

    private static final int BITMAP_SIZE = 512; // size of qr code

    /**
     * Method is used to generate QR Code
     *
     * @param content
     * @return
     * @throws WriterException
     */
    public static Bitmap generateQrCode(String content) throws WriterException {
        // set error correction level to 'H'
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // create QRCodeWriter
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, BITMAP_SIZE, BITMAP_SIZE, hintMap);

        // retrieve bitMatrix width and height
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        // create bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}
