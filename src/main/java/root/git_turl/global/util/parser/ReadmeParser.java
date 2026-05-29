package root.git_turl.global.util.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReadmeParser {
    public static String readReadme(String repoLocalPath) {
        String[] readmeNames = {"README.md", "readme.md", "README.MD", "Readme.md"};

        for (String name : readmeNames) {
            Path readmePath = Path.of(repoLocalPath, name);
            if (Files.exists(readmePath)) {
                try {
                    String content = Files.readString(readmePath);
                    // 너무 길면 앞 100줄만
                    String[] lines = content.split("\n");
                    return Arrays.stream(lines)
                            .limit(100)
                            .collect(Collectors.joining("\n"));
                } catch (IOException e) {
                    return "";
                }
            }
        }
        return "";
    }
}
