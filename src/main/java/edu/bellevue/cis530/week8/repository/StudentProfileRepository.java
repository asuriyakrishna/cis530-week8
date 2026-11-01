package edu.bellevue.cis530.week8.repository;

import edu.bellevue.cis530.week8.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}
