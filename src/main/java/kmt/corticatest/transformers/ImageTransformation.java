package kmt.corticatest.transformers;

import java.awt.image.BufferedImage;

/**
 * Created by assafe on 5/12/2016.
 */
public class ImageTransformation {
    public final BufferedImage image;
    public final String type;

    public ImageTransformation(BufferedImage image, String type) {
        this.image = image;
        this.type = type;
    }
}
