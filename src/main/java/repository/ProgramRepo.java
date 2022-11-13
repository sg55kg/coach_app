package repository;

import model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProgramRepo extends JpaRepository<Program, UUID> {
}
