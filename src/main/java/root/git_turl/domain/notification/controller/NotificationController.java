package root.git_turl.domain.notification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.code.NotificationSuccessCode;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.dto.NotificationSettingReqDto;
import root.git_turl.domain.notification.dto.NotificationSettingResDto;
import root.git_turl.domain.notification.service.NotificationService;
import root.git_turl.domain.notification.service.NotificationSettingService;
import root.git_turl.global.annotation.CurrentUser;
import root.git_turl.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController implements NotificationControllerDocs {

    private final NotificationService notificationService;
    private final NotificationSettingService notificationSettingService;

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

    @GetMapping("/settings")
    public ApiResponse<NotificationSettingResDto.NotificationSettingDto> getNotificationSetting(
            @CurrentUser Member member
    ) {
        return ApiResponse.onSuccess(
                NotificationSuccessCode.NOTIFICATION_SETTING_FOUND,
                notificationSettingService.getSetting(member)
        );
    }

    @PatchMapping("/settings")
    public ApiResponse<NotificationSettingResDto.NotificationSettingDto> updateNotificationSetting(
            @CurrentUser Member member,
            @RequestBody @Valid NotificationSettingReqDto.UpdateNotificationSettingReqDto request
    ) {
        return ApiResponse.onSuccess(
                NotificationSuccessCode.NOTIFICATION_SETTING_UPDATED,
                notificationSettingService.updateSetting(member, request)
        );
    }
}