package root.git_turl.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.dto.NotificationSettingReqDto;
import root.git_turl.domain.notification.dto.NotificationSettingResDto;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationControllerDocs {

    @Operation(
            summary = "SSE 알림 구독",
            description = """
                    실시간 알림을 수신하기 위한 SSE(Server-Sent Events) 연결 API입니다.

                    댓글 알림
                    - 게시글 작성자에게 알림

                    대댓글 알림
                    - 원댓글 작성자에게 알림
                    - 게시글 작성자에게 알림

                    로그인된 사용자만 연결 가능합니다.
                    """
    )
    SseEmitter subscribe(
            @CurrentUser Member member,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
            String lastEventId
    );

    @Operation(
            summary = "내 알림 목록 조회",
            description = "로그인한 사용자의 알림 목록을 최신순으로 조회합니다."
    )
    ApiResponse<List<NotificationResDto>> getNotifications(
            @CurrentUser Member member
    );

    @Operation(
            summary = "알림 읽음 처리",
            description = "특정 알림을 읽음 상태로 변경합니다."
    )
    ApiResponse<Void> readNotification(
            @CurrentUser Member member,
            @PathVariable Long notificationId
    );

    @Operation(
            summary = "알림 설정 조회",
            description = """
                    로그인한 사용자의 알림 설정을 조회합니다.

                    조회 항목
                    - 게시글 댓글 알림 수신 여부
                    - 대댓글 알림 수신 여부
                    """
    )
    ApiResponse<NotificationSettingResDto.NotificationSettingDto> getNotificationSetting(
            @CurrentUser Member member
    );

    @Operation(
            summary = "알림 설정 변경",
            description = """
                    로그인한 사용자의 알림 설정을 변경합니다.

                    한 번의 요청으로 다음 설정을 함께 변경합니다.
                    - 게시글 댓글 알림 수신 여부
                    - 대댓글 알림 수신 여부
                    """
    )
    ApiResponse<NotificationSettingResDto.NotificationSettingDto> updateNotificationSetting(
            @CurrentUser Member member,
            @RequestBody @Valid NotificationSettingReqDto.UpdateNotificationSettingReqDto request
    );
}