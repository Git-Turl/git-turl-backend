package root.git_turl.infrastructure.github;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class GitLogService {

    public String getCommitDiff(String repoPath, String hash) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "git",
                    "-C", repoPath,
                    "show",
                    hash
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(builder.start().getInputStream())
            );

            StringBuilder diff = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                diff.append(line).append("\n");
            }

            return diff.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}