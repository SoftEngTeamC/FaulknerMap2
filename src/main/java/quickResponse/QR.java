package quickResponse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

// From the ZXing API
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Created by sam on 4/21/17.
 */
public class QR {
    /**
     * @author sccoache
     *
     * @param directions
     * @throws IOException
     */
    public static Image buildQR(String directions) throws IOException, WriterException {
        //directions = "Successful"; //testing string
        String qrDisplayedText = directions;
        String filePath = "~/src/main/resources/images/";  //The path the .png will be saved to
        int size = 125;
        //String charset = "UTF-8";
        String fileType = "png";
        File qrFile = new File(filePath);

        return buildImage(qrFile, qrDisplayedText, size, fileType);
    }

    /**
     * @author: sccoache
     *
     */
    private static Image buildImage(File qrFile, String QRText, int size, String fileType) throws IOException,
            WriterException {

        // Create the BitMatrix
        Hashtable hintMap = new Hashtable();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        // Creates the Writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = null;

        // Attempts to encode the text directions as a byteMatrix
            byteMatrix = qrCodeWriter.encode(QRText, BarcodeFormat.QR_CODE, size, size, hintMap);


        // Define elements of the QR code for the BitMatrix
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);

        // Create the QR image
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        // Program outputs
        return SwingFXUtils.toFXImage(image, null); //alternative way to gain access to the QR image
        //ImageIO.write(image, fileType, qrFile); //writes the QR to the given file of the given type
    }
}
