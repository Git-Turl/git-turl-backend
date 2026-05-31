package root.git_turl.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.converter.NotificationSettingConverter;
import root.git_turl.domain.notification.dto.NotificationSettingReqDto;
import root.git_turl.domain.notification.dto.NotificationSettingResDto;
import root.git_turl.domain.notification.entity.NotificationSetting;
import root.git_turl.domain.notification.repository.NotificationSettingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final NotificationSettingConverter notificationSettingConverter;

    public NotificationSettingResDto.NotificationSettingDto getSetting(Member member) {
        NotificationSetting setting = getOrCreateSetting(member);

        return notificationSettingConverter.toDto(setting);
    }

    @Transactional
    public NotificationSettingResDto.NotificationSettingDto updateSetting(
            Member member,
            NotificationSettingReqDto.UpdateNotificationSettingReqDto request
    ) {
        NotificationSetting setting = getOrCreateSetting(member);

        setting.update(
                request.getCommentNotificationEnabled(),
                request.getReplyNotificationEnabled()
        );

        return notificationSettingConverter.toDto(setting);
    }

    @Transactional
    public NotificationSetting getOrCreateSetting(Member member) {
        return notificationSettingRepository.findByMember(member)
                .orElseGet(() -> notificationSettingRepository.save(
                        notificationSettingConverter.toDefaultSetting(member)
                ));
    }

    public boolean isCommentNotificationEnabled(Member member) {
        return getOrCreateSetting(member).getCommentNotificationEnabled();
    }

    public boolean isReplyNotificationEnabled(Member member) {
        return getOrCreateSetting(member).getReplyNotificationEnabled();
    }
}