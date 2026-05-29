package root.git_turl.global.util;

public class GitRepoParser {
    public static String getRepoFullName(String repoLink) {
        String[] list = repoLink.split("/");
        return list[3] + "/" + list[4].split("\\.")[0];
    }

    public static String getRepoLink(String repoFullName) {
        return "https://github.com/" + repoFullName + ".git";
    }
}
