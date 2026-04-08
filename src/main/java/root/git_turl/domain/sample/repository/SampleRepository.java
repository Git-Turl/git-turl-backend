package root.git_turl.domain.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.sample.entity.SampleEntity;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
