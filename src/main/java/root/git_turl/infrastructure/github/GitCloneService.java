package root.git_turl.infrastructure.github;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("git clone 실패");
            }

            return repoDir;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("git clone 실행 중 오류", e);
        }
    }
}