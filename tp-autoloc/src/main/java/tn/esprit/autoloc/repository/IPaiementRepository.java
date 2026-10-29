package tn.esprit.autoloc.repository;

import tn.esprit.autoloc.domain.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}