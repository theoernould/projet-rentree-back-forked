package imt.deploy.deployer.repositories;

import imt.deploy.deployer.models.Pipeline;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PipelineRepository extends MongoRepository<Pipeline, String> {
    Optional<Pipeline> findFirstByOrderByNumberDesc();
    List<Pipeline> findAllByOrderByNumberDesc();
}
