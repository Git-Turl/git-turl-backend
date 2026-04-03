package root.git_turl.domain.sample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.sample.code.SampleErrorCode;
import root.git_turl.domain.sample.dto.SampleCreateRequest;
import root.git_turl.domain.sample.dto.SampleResponse;
import root.git_turl.domain.sample.dto.SampleUpdateRequest;
import root.git_turl.domain.sample.entity.SampleEntity;
import root.git_turl.domain.sample.exception.SampleException;
import root.git_turl.domain.sample.repository.SampleRepository;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional
    public SampleResponse createSample(SampleCreateRequest request) {
        SampleEntity sample = SampleEntity.builder()
                .title(request.title())
                .content(request.content())
                .build();

        sampleRepository.save(sample);
        return SampleResponse.builder()
                .id(sample.getId())
                .title(sample.getTitle())
                .content(sample.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public SampleResponse getSample(Long id) {
        SampleEntity sample = sampleRepository.findById(id)
                .orElseThrow(() -> new SampleException(SampleErrorCode.NOT_FOUND));

        return SampleResponse.builder()
                .id(sample.getId())
                .title(sample.getTitle())
                .content(sample.getContent())
                .build();
    }

    @Transactional
    public SampleResponse updateSample(Long id, SampleUpdateRequest request) {
        SampleEntity sample = sampleRepository.findById(id)
                .orElseThrow(() -> new SampleException(SampleErrorCode.NOT_FOUND));

        sample.patchSample(request.title(), request.content());

        return SampleResponse.builder()
                .id(sample.getId())
                .title(sample.getTitle())
                .content(sample.getContent())
                .build();
    }

    @Transactional
    public void deleteSample(Long id) {
        if (!sampleRepository.existsById(id)){
            throw new SampleException(SampleErrorCode.NOT_FOUND);
        }

        sampleRepository.deleteById(id);
    }

}
