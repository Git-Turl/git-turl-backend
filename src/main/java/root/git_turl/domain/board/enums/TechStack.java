package root.git_turl.domain.board.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechStack {

    // FRONTEND
    HTML_CSS(TechField.FRONTEND),
    TAILWIND_CSS(TechField.FRONTEND),
    JAVASCRIPT(TechField.FRONTEND),
    TYPESCRIPT(TechField.FRONTEND),
    REACT(TechField.FRONTEND),
    VUE(TechField.FRONTEND),
    NEXT_JS(TechField.FRONTEND),

    // BACKEND
    JAVA(TechField.BACKEND),
    SPRING(TechField.BACKEND),
    SPRING_BOOT(TechField.BACKEND),
    NODE_JS(TechField.BACKEND),
    EXPRESS(TechField.BACKEND),
    MYSQL(TechField.BACKEND),
    POSTGRESQL(TechField.BACKEND),

    // AI
    TENSORFLOW(TechField.BACKEND),
    PYTORCH(TechField.BACKEND),
    SCIKIT_LEARN(TechField.BACKEND),
    LANGCHAIN(TechField.BACKEND),
    OPENAI_API(TechField.BACKEND),
    PANDAS(TechField.BACKEND),;

    private final TechField techField;
}
