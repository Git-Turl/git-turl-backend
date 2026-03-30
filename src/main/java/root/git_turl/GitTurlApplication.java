package root.git_turl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class GitTurlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitTurlApplication.class, args);
	}

}
