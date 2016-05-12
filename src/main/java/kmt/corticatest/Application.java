package kmt.corticatest;

import kmt.corticatest.db.ConfigDBUsageSample;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws SQLException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		ConfigDBUsageSample configDB = new ConfigDBUsageSample("jdbc:hsqldb:mem:corticatestDb", "SA", "", "org.hsqldb.jdbc.JDBCDriver");
		configDB.createTable();

		ImageDownloader imageDownloader = context.getBean(ImageDownloader.class);
		InputStream inputImages = Application.class.getResourceAsStream("/input.images.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputImages));
		reader.lines().forEach(line -> {
			try {
				ImageDownloader.DownloadResult downloadResult = imageDownloader.download(line);
				try {
					configDB.insertIntoTable(
							downloadResult.downloadDate,
							downloadResult.fileLocation.toString(),
							downloadResult.origUrl,
							new String(MessageDigest.getInstance("MD5").digest(downloadResult.origUrl.getBytes())));
				} catch (SQLException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.HSQL)
				.setName("corticatestDb")
				.build();
	}
}
