package tn.esprit.autoloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.IVehiculeService;

@SpringBootTest
class ServiceTests {

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IMaintenanceService maintenanceService;

    @Autowired
    private IReservationService reservationService;

    // ===================== Partie 2 : Agence =====================
    @Test
    void testAgenceService() {
        System.out.println("\n===== TEST AGENCE SERVICE =====");

        // create
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        Agence saved = agenceService.create(agence);
        assertNotNull(saved.getId());
        System.out.println("Agence créée : id=" + saved.getId()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());

        // findAll
        List<Agence> agences = agenceService.findAll();
        assertFalse(agences.isEmpty());
        System.out.println("Liste des agences :");
        agences.forEach(a -> System.out.println("  - " + a.getId() + " | " + a.getNom() + " | " + a.getVille()));

        // findById
        Agence trouvee = agenceService.findById(saved.getId());
        assertEquals("AutoLoc Tunis Centre", trouvee.getNom());
        System.out.println("findById(" + saved.getId() + ") : " + trouvee.getNom());

        // update
        Agence modif = new Agence();
        modif.setNom("AutoLoc Tunis Lac");
        modif.setVille("Tunis");
        modif.setAdresse("Rue du Lac Léman");
        modif.setTelephone("71111111");
        Agence maj = agenceService.update(saved.getId(), modif);
        assertEquals("AutoLoc Tunis Lac", maj.getNom());
        System.out.println("Après update : nom=" + maj.getNom() + ", adresse=" + maj.getAdresse());

        // count
        System.out.println("Nombre d'agences : " + agenceService.findAll().size());

        // deleteById + vérification
        Long id = saved.getId();
        agenceService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> agenceService.findById(id));
        System.out.println("Agence " + id + " supprimée : elle n'existe plus.");
    }

    // ===================== Partie 3 : Client =====================
    @Test
    void testClientService() {
        System.out.println("\n===== TEST CLIENT SERVICE =====");

        // create (2 clients)
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.tn");
        c1.setTelephone("22000000");
        c1.setNumPermis("P-12345");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Amira");
        c2.setEmail("amira.trabelsi@mail.tn");
        c2.setTelephone("55000000");
        c2.setNumPermis("P-67890");
        c2.setDateInscription(LocalDate.now());

        Client s1 = clientService.create(c1);
        Client s2 = clientService.create(c2);
        assertNotNull(s1.getId());
        assertNotNull(s2.getId());
        System.out.println("Clients créés : id=" + s1.getId() + " et id=" + s2.getId());

        // findAll
        List<Client> clients = clientService.findAll();
        System.out.println("Liste des clients :");
        clients.forEach(c -> System.out.println("  - " + c.getId() + " | " + c.getNom() + " " + c.getPrenom() + " | " + c.getEmail()));

        // findById
        Client trouve = clientService.findById(s1.getId());
        assertEquals("sami.benali@mail.tn", trouve.getEmail());
        System.out.println("findById(" + s1.getId() + ") : " + trouve.getNom() + " " + trouve.getPrenom());

        // update
        Client modif = new Client();
        modif.setNom("Ben Ali");
        modif.setPrenom("Sami");
        modif.setEmail("sami.nouveau@mail.tn");
        modif.setTelephone("22999999");
        modif.setNumPermis("P-12345");
        modif.setDateInscription(trouve.getDateInscription());
        Client maj = clientService.update(s1.getId(), modif);
        assertEquals("sami.nouveau@mail.tn", maj.getEmail());
        System.out.println("Après update : email=" + maj.getEmail());

        // count
        System.out.println("Nombre de clients : " + clientService.findAll().size());

        // deleteById + vérification (on supprime les deux pour garder la base propre)
        Long id1 = s1.getId();
        Long id2 = s2.getId();
        clientService.deleteById(id1);
        clientService.deleteById(id2);
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(id1));
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(id2));
        System.out.println("Clients " + id1 + " et " + id2 + " supprimés : ils n'existent plus.");
    }

    // ===================== Partie 4 : Vehicule =====================
    @Test
    void testVehiculeService() {
        System.out.println("\n===== TEST VEHICULE SERVICE =====");

        // create
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TU 4567");
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setTarifJournalier(new BigDecimal("90.00"));

        Vehicule saved = vehiculeService.create(v);
        assertNotNull(saved.getIdVehicule());
        System.out.println("Véhicule créé : id=" + saved.getIdVehicule()
                + ", " + saved.getMarque() + " " + saved.getModele() + ", tarif=" + saved.getTarifJournalier());

        // findAll
        List<Vehicule> vehicules = vehiculeService.findAll();
        System.out.println("Liste des véhicules :");
        vehicules.forEach(x -> System.out.println("  - " + x.getIdVehicule() + " | " + x.getImmatriculation() + " | " + x.getMarque() + " " + x.getModele()));

        // findById
        Vehicule trouve = vehiculeService.findById(saved.getIdVehicule());
        assertEquals("123 TU 4567", trouve.getImmatriculation());
        System.out.println("findById(" + saved.getIdVehicule() + ") : " + trouve.getImmatriculation());

        // update
        Vehicule modif = new Vehicule();
        modif.setImmatriculation("123 TU 4567");
        modif.setMarque("Peugeot");
        modif.setModele("208 GT");
        modif.setTarifJournalier(new BigDecimal("110.00"));
        Vehicule maj = vehiculeService.update(saved.getIdVehicule(), modif);
        assertEquals("208 GT", maj.getModele());
        System.out.println("Après update : modele=" + maj.getModele() + ", tarif=" + maj.getTarifJournalier());

        // règle métier : tarif négatif refusé
        Vehicule invalide = new Vehicule();
        invalide.setImmatriculation("999 TU 9999");
        invalide.setTarifJournalier(new BigDecimal("-10"));
        assertThrows(IllegalArgumentException.class, () -> vehiculeService.create(invalide));
        System.out.println("Règle métier OK : un tarif négatif est refusé.");

        // count
        System.out.println("Nombre de véhicules : " + vehiculeService.findAll().size());

        // deleteById + vérification
        Long id = saved.getIdVehicule();
        vehiculeService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.findById(id));
        System.out.println("Véhicule " + id + " supprimé : il n'existe plus.");
    }

    // ===================== Partie 5 : exercice autonome =====================

    @Test
    void testEquipementService() {
        System.out.println("\n===== TEST EQUIPEMENT SERVICE =====");

        Equipement e = new Equipement();
        e.setLibelle("GPS");
        Equipement saved = equipementService.create(e);
        assertNotNull(saved.getId());
        System.out.println("Équipement créé : id=" + saved.getId() + ", libellé=" + saved.getLibelle());

        Equipement trouve = equipementService.findById(saved.getId());
        assertEquals("GPS", trouve.getLibelle());
        System.out.println("findById(" + saved.getId() + ") : " + trouve.getLibelle());
        System.out.println("Nombre d'équipements : " + equipementService.findAll().size());

        // règle métier : libellé obligatoire
        Equipement invalide = new Equipement();
        assertThrows(IllegalArgumentException.class, () -> equipementService.create(invalide));
        System.out.println("Règle métier OK : un équipement sans libellé est refusé.");

        Long id = saved.getId();
        equipementService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> equipementService.findById(id));
        System.out.println("Équipement " + id + " supprimé : il n'existe plus.");
    }

    @Test
    void testMaintenanceService() {
        System.out.println("\n===== TEST MAINTENANCE SERVICE =====");

        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.of(2026, 11, 1));
        m.setDateFin(LocalDate.of(2026, 11, 3));
        m.setDescription("Vidange et changement des plaquettes");
        Maintenance saved = maintenanceService.create(m);
        assertNotNull(saved.getId());
        System.out.println("Maintenance créée : id=" + saved.getId() + ", " + saved.getDescription());

        List<Maintenance> maintenances = maintenanceService.findAll();
        assertFalse(maintenances.isEmpty());
        System.out.println("Nombre de maintenances : " + maintenances.size());

        // règle métier : date de fin avant date de début
        Maintenance invalide = new Maintenance();
        invalide.setDateDebut(LocalDate.of(2026, 11, 10));
        invalide.setDateFin(LocalDate.of(2026, 11, 5));
        assertThrows(IllegalArgumentException.class, () -> maintenanceService.create(invalide));
        System.out.println("Règle métier OK : une date de fin antérieure est refusée.");

        Long id = saved.getId();
        maintenanceService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.findById(id));
        System.out.println("Maintenance " + id + " supprimée : elle n'existe plus.");
    }

    @Test
    void testReservationService() {
        System.out.println("\n===== TEST RESERVATION SERVICE =====");

        Reservation r = new Reservation();
        r.setDateDebut(LocalDate.of(2026, 12, 20));
        r.setDateFin(LocalDate.of(2026, 12, 27));
        Reservation saved = reservationService.create(r);
        assertNotNull(saved.getId());
        System.out.println("Réservation créée : id=" + saved.getId()
                + ", du " + saved.getDateDebut() + " au " + saved.getDateFin());

        Reservation trouvee = reservationService.findById(saved.getId());
        assertEquals(LocalDate.of(2026, 12, 27), trouvee.getDateFin());
        System.out.println("findById(" + saved.getId() + ") : fin le " + trouvee.getDateFin());
        System.out.println("Nombre de réservations : " + reservationService.findAll().size());

        Long id = saved.getId();
        reservationService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> reservationService.findById(id));
        System.out.println("Réservation " + id + " supprimée : elle n'existe plus.");
    }
}