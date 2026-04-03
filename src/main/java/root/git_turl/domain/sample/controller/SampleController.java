package root.git_turl.domain.sample.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import root.git_turl.domain.sample.code.SampleSuccessCode;
import root.git_turl.domain.sample.dto.SampleCreateRequest;
import root.git_turl.domain.sample.dto.SampleResponse;
import root.git_turl.domain.sample.dto.SampleUpdateRequest;
import root.git_turl.domain.sample.service.SampleService;
import root.git_turl.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/samples")
@Tag(name = "Sample", description = "샘플 API")
public class SampleController {

    private final SampleService sampleService;

    @PostMapping
    public ApiResponse<SampleResponse> createSample(
            @Valid @RequestBody SampleCreateRequest request
    ) {
        SampleResponse response = sampleService.createSample(request);
        return ApiResponse.onSuccess(SampleSuccessCode.CREATE_OK, response);
    }

    @GetMapping("/{id}")
    public ApiResponse<SampleResponse> getSample(
            @PathVariable Long id
    ) {
        SampleResponse response = sampleService.getSample(id);
        return ApiResponse.onSuccess(SampleSuccessCode.GET_OK, response);
    }

    @PatchMapping("/{id}")
    public ApiResponse<SampleResponse> updateSample(
            @PathVariable Long id,
            @Valid @RequestBody SampleUpdateRequest request
    ) {
        SampleResponse response = sampleService.updateSample(id, request);
        return ApiResponse.onSuccess(SampleSuccessCode.PATCH_OK, response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSample(
            @PathVariable Long id
    ) {
        sampleService.deleteSample(id);
        return ApiResponse.onSuccess(SampleSuccessCode.DELETE_OK, null);
    }
}
