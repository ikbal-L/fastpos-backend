package com.softlines.fastpos.repository;
import com.softlines.fastpos.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestEntityRepository extends JpaRepository<TestEntity, UUID> {
}
