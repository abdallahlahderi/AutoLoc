package tn.esprit.autoloc.repository;

import tn.esprit.autoloc.domain.Employe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}