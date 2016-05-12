package kmt.corticatest.transformers;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by assafe on 5/12/2016.
 */
@Component
public class ImageGrayer implements ImageTransformer {
    @Override
    public ImageTransformation transform(ImageTransformation imageTransformation) {

        // create output image
        BufferedImage outputImage =
            new BufferedImage(imageTransformation.image.getWidth(), imageTransformation.image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(imageTransformation.image, 0, 0, null);
        g2d.dispose();

        return new ImageTransformation(outputImage, imageTransformation.type);
    }
}
