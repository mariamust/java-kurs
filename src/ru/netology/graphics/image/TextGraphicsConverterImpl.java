package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;
    private int newWidth;
    private int newHeight;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));

        int imgHeight = img.getHeight();
        int imgWidth = img.getWidth();

        double ratio = (double) imgHeight / imgWidth;
        if (maxRatio > 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        maxHeight = 300;
        maxWidth = 300;
        double compressRatioHeight = (double) this.maxHeight / imgHeight;
        double compressRatioWidth = (double) this.maxWidth / imgWidth;
        double compressRatio;

        if (compressRatioWidth >= 1 && compressRatioHeight >= 1) {
            compressRatio = 1;
        } else {
            compressRatio = Math.min(compressRatioWidth, compressRatioHeight);
        }
        newWidth = (int) (imgWidth * compressRatio);
        newHeight = (int) (imgHeight * compressRatio);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        int[] colorIntensity = new int[3];

        Schema schema = new Schema();
        StringBuilder sb = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, colorIntensity)[0];
                char c = schema.convert(color);
                sb.append(c);
                sb.append(c);
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}

