package root.git_turl.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.context.ApplicationEventPublisher;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.comment.entity.Comment;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.notification.code.NotificationErrorCode;
import root.git_turl.domain.notification.converter.NotificationConverter;
import root.git_turl.domain.notification.dto.NotificationResDto;
import root.git_turl.domain.notification.entity.Notification;
import root.git_turl.domain.notification.enums.NotificationTag;
import root.git_turl.domain.notification.enums.NotificationTargetType;
import root.git_turl.domain.notification.exception.NotificationException;
import root.git_turl.domain.notification.repository.EmitterRepository;
import root.git_turl.domain.notification.repository.NotificationRepository;
import root.git_turl.domain.notification.event.NotificationCreatedEvent;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    private final NotificationSettingService notificationSettingService;
    private final ApplicationEventPublisher eventPublisher;

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = makeEmitterId(memberId);

        SseEmitter emitter = emitterRepository.save(
                emitterId,
                new SseEmitter(DEFAULT_TIMEOUT)
        );

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        sendToClient(emitter, emitterId, "connect", "SSE 연결 완료");

        return emitter;
    }

    @Transactional
    public void sendCommentNotification(Member actor, Board board, Comment comment) {
        Member receiver = board.getMember();

        if (actor.getId().equals(receiver.getId())) {
            return;
        }

        if (!notificationSettingService.isCommentNotificationEnabled(receiver)) {
            return;
        }

        Notification notification = saveNotification(
                actor,
                receiver,
                board.getId(),
                comment.getId(),
                NotificationTag.COMMENT,
                comment.getContent()
        );

        publishNotification(receiver.getId(), notification);
    }

    @Transactional
    public void sendReplyNotification(Member actor, Board board, Comment parentComment, Comment replyComment) {
        Member parentCommentWriter = parentComment.getMember();
        Member boardWriter = board.getMember();

        if (!actor.getId().equals(parentCommentWriter.getId())
                && notificationSettingService.isReplyNotificationEnabled(parentCommentWriter)) {

            Notification notification = saveNotification(
                    actor,
                    parentCommentWriter,
                    board.getId(),
                    parentComment.getId(),
                    NotificationTag.REPLY,
                    replyComment.getContent()
            );

            publishNotification(parentCommentWriter.getId(), notification);
        }

        if (!actor.getId().equals(boardWriter.getId())
                && !boardWriter.getId().equals(parentCommentWriter.getId())
                && notificationSettingService.isReplyNotificationEnabled(boardWriter)) {

            Notification notification = saveNotification(
                    actor,
                    boardWriter,
                    board.getId(),
                    parentComment.getId(),
                    NotificationTag.REPLY,
                    replyComment.getContent()
            );

            publishNotification(boardWriter.getId(), notification);
        }
    }

    private Notification saveNotification(
            Member actor,
            Member receiver,
            Long boardId,
            Long targetObjectId,
            NotificationTag tag,
            String previewContent
    ) {
        Notification notification = Notification.builder()
                .actor(actor)
                .receiver(receiver)
                .boardId(boardId)
                .targetObjectId(targetObjectId)
                .targetType(NotificationTargetType.BOARD)
                .tag(tag)
                .previewContent(makePreviewContent(previewContent))
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    private void publishNotification(
            Long receiverId,
            Notification notification
    ) {
        NotificationResDto response =
                notificationConverter.toNotificationResDto(notification);

        eventPublisher.publishEvent(
                new NotificationCreatedEvent(receiverId, response)
        );
    }

    private void sendToClient(
            SseEmitter emitter,
            String emitterId,
            String eventName,
            Object data
    ) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name(eventName)
                            .data(data)
            );
        } catch (IOException | IllegalStateException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private String makeEmitterId(Long memberId) {
        return memberId + "_notification";
    }

    private String makePreviewContent(String content) {
        if (content == null) {
            return "";
        }

        if (content.length() <= 30) {
            return content;
        }

        return content.substring(0, 30);
    }

    @Transactional(readOnly = true)
    public List<NotificationResDto> getNotifications(Member member) {
        return notificationRepository.findAllDtoByReceiver(member);
    }

    @Transactional
    public void readNotification(Member member, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND)
                );

        if (!notification.getReceiver().getId().equals(member.getId())) {
            throw new NotificationException(
                    NotificationErrorCode.NO_NOTIFICATION_PERMISSION
            );
        }

        notification.read();
    }
}