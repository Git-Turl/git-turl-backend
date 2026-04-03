package root.git_turl.domain.sample.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import root.git_turl.domain.sample.dto.SampleCreateRequest;
import root.git_turl.domain.sample.dto.SampleResponse;
import root.git_turl.global.apiPayload.ApiResponse;

public interface SampleControllerDocs {

    @Operation(
            summary = "샘플 생성",
            description = "샘플 데이터를 생성합니다."
    )
    public ApiResponse<SampleResponse> createSample(
            @Valid @RequestBody SampleCreateRequest request
    );

    @Operation(
            summary = "샘플 단건 조회",
            description = "샘플 ID를 이용해 단건 조회합니다."
    )
    public ApiResponse<SampleResponse> getSample(
            @PathVariable Long id
    );

    @Operation(
            summary = "샘플 수정",
            description = "샘플 ID에 해당하는 데이터를 수정합니다."
    )
    public ApiResponse<SampleResponse> updateSample(
            @PathVariable Long id
    );

    @Operation(
            summary = "샘플 삭제",
            description = "샘플 ID에 해당하는 데이터를 삭제합니다."
    )
    public ApiResponse<Void> deleteSample(
            @PathVariable Long id
    );
}
