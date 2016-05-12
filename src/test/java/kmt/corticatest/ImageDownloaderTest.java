package kmt.corticatest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by assafe on 5/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ImageDownloaderTest {

    @Autowired
    private ImageDownloader downloader;

    public ImageDownloaderTest() {
    }

    @Test public void verifyImageDownloadedProperlyWhenUrlIsValid() throws IOException {
        ImageDownloader.DownloadResult result = downloader.download(validImageUrl());
    }

    private String validImageUrl() {
        return "http://carbl.com/im/2013/07/Suzuki-Swift-5d-600x324.jpg";
    }
}