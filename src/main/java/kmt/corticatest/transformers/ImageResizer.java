package kmt.corticatest.transformers;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by assafe on 5/12/2016.
 */
@Component
public class ImageResizer implements ImageTransformer {
    @Override
    public ImageTransformation transform(ImageTransformation imageTransformation) {
        int scaleToWidth = 200;
        int scaleToHeight = 200;
        // create output image
        BufferedImage outputImage = new BufferedImage(scaleToWidth, scaleToHeight, imageTransformation.image.getType());

        // scale the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(imageTransformation.image, 0, 0, scaleToWidth, scaleToHeight, null);
        g2d.dispose();

        return new ImageTransformation(outputImage, imageTransformation.type);
    }
}
