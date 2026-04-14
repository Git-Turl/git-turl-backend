package root.git_turl.global.util;

import root.git_turl.domain.report.dto.commit.GitCommit;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class GitLogParser {

    public List<GitCommit> getCommits(String repoPath) {
        List<GitCommit> commits = new ArrayList<>();

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "git",
                    "-C", repoPath,
                    "log",
                    "--pretty=format:%H|%an|%ad|%s",
                    "--date=short"
            );

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);

                if (parts.length < 4) continue;

                GitCommit commit = new GitCommit(
                        parts[0], // hash
                        parts[1], // author
                        LocalDate.parse(parts[2]), // date
                        parts[3]  // message
                );

                commits.add(commit);
            }

            process.waitFor();

        } catch (Exception e) {
            throw new RuntimeException("git log 실행 실패", e);
        }

        return commits;
    }
}
