package root.git_turl.domain.notification.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import root.git_turl.domain.notification.repository.EmitterRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EmitterRepository emitterRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotificationCreated(
            NotificationCreatedEvent event
    ) {
        String emitterId = makeEmitterId(event.receiverId());

        emitterRepository.findById(emitterId)
                .ifPresent(emitter -> sendToClient(
                        emitter,
                        emitterId,
                        event.notification()
                ));
    }

    private void sendToClient(
            SseEmitter emitter,
            String emitterId,
            Object data
    ) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name("notification")
                            .data(data)
            );
        } catch (IOException | IllegalStateException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private String makeEmitterId(Long memberId) {
        return memberId + "_notification";
    }
}