package root.git_turl.global.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.exception.MemberException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsFileService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveProfileImg(MultipartFile multipartFile, Long memberId) throws IOException {
        return uploadProfileImg(multipartFile, memberId);
    }

    public String uploadProfileImg(MultipartFile file, Long memberId) throws IOException {
        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {
            throw new MemberException(MemberErrorCode.FILE_TYPE_ERROR);
        }

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "profile/" + memberId + "/" + UUID.randomUUID() + ext;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return getPublicUrl(fileName);
    }

    public String uploadVoiceFile(MultipartFile file, Long memberId, Long questionId) throws IOException {

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("audio/")) {
            throw new MemberException(MemberErrorCode.FILE_TYPE_ERROR);
        }

        String originalName = file.getOriginalFilename();
        System.out.println(originalName);
        System.out.println(file.getContentType());
        String ext = originalName.substring(originalName.lastIndexOf("."));

        String fileName = "voiceFile/" + memberId + "/questions/" + questionId + UUID.randomUUID() + ext;

        String contentType = normalizeContentType(file.getContentType(), ext);

        byte[] bytes = file.getBytes();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(contentType)
                .contentLength((long) bytes.length)
                .contentDisposition("inline")
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(bytes)
        );

        return getPublicUrl(fileName);
    }

    private String getPublicUrl(String key) {
        return "https://" + bucket + ".s3.amazonaws.com/" + key;
    }

    private String normalizeContentType(String contentType, String ext) {

        if (contentType == null) return "audio/mp4";

        return switch (ext.toLowerCase()) {
            case ".mp3" -> "audio/mpeg";
            case ".wav" -> "audio/wav";
            case ".m4a" -> "audio/mp4";
            case ".webm" -> "audio/webm";
            default -> contentType.replace("x-m4a", "mp4");
        };
    }
}
