package kharebov.skill.finalproject.repositories;

import kharebov.skill.finalproject.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationsRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByOwnerId(Long id);
}
