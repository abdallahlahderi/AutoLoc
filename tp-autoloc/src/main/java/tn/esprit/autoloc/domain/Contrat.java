package tn.esprit.autoloc.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import tn.esprit.autoloc.domain.enums.*;


@Entity
@Table(name = "Contrat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSignature;

    private BigDecimal montantTotal;

    private boolean valide = false;

}