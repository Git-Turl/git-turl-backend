package root.git_turl.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
        return emitter;
    }

    public Optional<SseEmitter> findById(String emitterId) {
        return Optional.ofNullable(emitters.get(emitterId));
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }
}
