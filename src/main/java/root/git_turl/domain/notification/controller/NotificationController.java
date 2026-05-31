package root.git_turl.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.code.NotificationSuccessCode;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.service.NotificationService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController implements NotificationControllerDocs {

    private final NotificationService notificationService;

    @Override
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @CurrentUser Member member,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        return notificationService.subscribe(member.getId(), lastEventId);
    }

    @Override
    @GetMapping
    public ApiResponse<List<NotificationResDto>> getNotifications(
            @CurrentUser Member member
    ) {
        return ApiResponse.onSuccess(
                NotificationSuccessCode.NOTIFICATION_LIST_FOUND,
                notificationService.getNotifications(member)
        );
    }

    @Override
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<Void> readNotification(
            @CurrentUser Member member,
            @PathVariable Long notificationId
    ) {
        notificationService.readNotification(member, notificationId);
        return ApiResponse.onSuccess(
                NotificationSuccessCode.NOTIFICATION_READ,
                null
        );
    }
}