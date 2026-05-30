package root.git_turl.infrastructure.github;

import org.springframework.stereotype.Service;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.exception.ReportException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@Service
public class GitCloneService {

    private static final String BASE_DIR = System.getProperty("java.io.tmpdir") + "/repos";

    public String cloneRepository(String gitUrl) {
        String repoDir = BASE_DIR + "/" + UUID.randomUUID();

        try {
            File dir = new File(BASE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            ProcessBuilder builder = new ProcessBuilder(
                    "git", "clone",
                    "--depth=100",
                    gitUrl,
                    repoDir
            );

            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                String errorMessage = output.toString();

                if (errorMessage.contains("Repository not found")) {
                    throw new ReportException(ReportErrorCode.REPO_NOT_FOUND);
                } else if (errorMessage.contains("Authentication failed")) {
                    throw new IllegalStateException("레포지토리 접근 권한이 없습니다.");
                } else {
                    throw new RuntimeException("git clone 실패: " + errorMessage);
                }
            }

            return repoDir;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("git clone 실행 중 오류", e);
        }
    }
}