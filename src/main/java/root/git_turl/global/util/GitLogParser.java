package root.git_turl.global.util;

import lombok.RequiredArgsConstructor;
import root.git_turl.domain.report.dto.commit.GitCommit;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public class GitLogParser {

    private final GithubUserMapper githubUserMapper;

    public List<GitCommit> getCommits(String repoPath) {
        List<GitCommit> commits = new ArrayList<>();

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "git",
                    "-C", repoPath,
                    "log",
                    "--pretty=format:%H|%an|%ae|%ad|%s",
                    "--date=short"
            );

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);

                if (parts.length < 5) continue;

                GitCommit commit = new GitCommit(
                        parts[0], // hash
                        parts[1], // author name
                        parts[2], // author email
                        LocalDate.parse(parts[3]), // date
                        parts[4]  // message
                );

                commits.add(commit);
            }

            process.waitFor();

        } catch (Exception e) {
            throw new RuntimeException("git log 실행 실패", e);
        }

        return commits;
    }

    public String getLogin(String email, String repoFullName) {
        String login = null;

        if (email.contains("@users.noreply.github.com")) {
            login = extractLogin(email);
        }
        if (login == null) {
            login = githubUserMapper.resolveLogin(email, repoFullName);
        }
        return login;
    }

    public static String extractLogin(String email) {
        if (email == null) return null;

        if (email.endsWith("@users.noreply.github.com") && email.contains("+")) {
            try {
                return email.split("\\+")[1].split("@")[0];
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}
