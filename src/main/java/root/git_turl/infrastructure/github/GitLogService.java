package root.git_turl.infrastructure.github;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GitLogService {

    public String getCommitDiff(String repoPath, String hash) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "git",
                    "-C", repoPath,
                    "show",
                    "--patch",          // 실제 코드 +/- 라인 포함
                    "--stat",           // 파일 요약도 함께
                    "--oneline",
                    "-U3",              // 변경 전후 3줄씩만 (토큰 절약)
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

    public String getNumstat(String repoPath, String commitHash) {
        List<String> command = List.of(
                "git", "log", "-1", "--numstat", "--format=", commitHash
        );

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(repoPath));
            pb.redirectErrorStream(false);

            Process process = pb.start();

            String output;
            try (InputStream is = process.getInputStream()) {
                output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.warn("git numstat 명령 타임아웃: hash={}", commitHash);
                return "";
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                log.warn("git numstat 명령 실패: hash={}, exitCode={}", commitHash, exitCode);
                return "";
            }

            return output;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("git numstat 실행 중 예외: hash={}", commitHash, e);
            return "";
        }
    }

    public int countCurrentFiles(String repoPath) {
        List<String> command = List.of("git", "ls-files");

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(repoPath));
            pb.redirectErrorStream(false);

            Process process = pb.start();

            String output;
            try (InputStream is = process.getInputStream()) {
                output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.warn("git ls-files 명령 타임아웃: repoPath={}", repoPath);
                return 0;
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                log.warn("git ls-files 명령 실패: repoPath={}, exitCode={}", repoPath, exitCode);
                return 0;
            }

            if (output.isBlank()) return 0;

            return (int) output.lines()
                    .filter(line -> !line.isBlank())
                    .count();

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("git ls-files 실행 중 예외: repoPath={}", repoPath, e);
            return 0;
        }
    }
}