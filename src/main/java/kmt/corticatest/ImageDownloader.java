package kmt.corticatest;

import kmt.corticatest.transformers.ImageTransformation;
import kmt.corticatest.transformers.ImageTransformationChain;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;

/**
 * Created by assafe on 5/12/2016.
 */
@Component
public class ImageDownloader {

    private final Logger log = Logger.getLogger(ImageDownloader.class);

    private final ImageTransformationChain imageTransformationChain;
    private final String downloadPath;

    @Autowired
    public ImageDownloader(ImageTransformationChain imageTransformationChain,
                           @Value("${kmt.corticatest.imageDownloader.downloadPath}") String downloadPathStr) throws IOException {
        this.imageTransformationChain = imageTransformationChain;
        this.downloadPath = downloadPathStr;
        Files.createDirectories(Paths.get(downloadPath));
    }

    public DownloadResult download(String imageUrl) throws IOException {
        // read input image
        URL url = new URL(imageUrl);
        log.info("downloading " + imageUrl);
        new ImageTransformation(ImageIO.read(url), extractType(imageUrl));
        ImageTransformation imageTransformation =
            imageTransformationChain.transform(new ImageTransformation(ImageIO.read(url), extractType(imageUrl)));
        String outputImagePath = downloadPath + File.separator + extractImageName(imageUrl) + "." + imageTransformation.type;
        ImageIO.write(imageTransformation.image, imageTransformation.type, new File(outputImagePath));
        log.info(imageUrl + " saved after transformations as " + outputImagePath);
        return new DownloadResult(
                new Date(System.currentTimeMillis()),
                Paths.get(outputImagePath),
                imageUrl);
    }

    private String extractImageName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
    }

    private String extractType(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
    }

    class Scale {
        public final int width;
        public final int height;
        public Scale(int x, int y) {
            this.width = x;
            this.height = y;
        }
    }

    class DownloadResult {
        public final Date downloadDate;
        public final Path fileLocation;
        public final String origUrl;

        public DownloadResult(Date downloadDate, Path fileLocation, String origUrl) {
            this.downloadDate = downloadDate;
            this.fileLocation = fileLocation;
            this.origUrl = origUrl;
        }
    }
}
