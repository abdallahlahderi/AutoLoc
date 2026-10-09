package tn.esprit.autoloc;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.repository.*;

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired IAgenceRepository agenceRepository;
    @Autowired IClientRepository clientRepository;
    @Autowired IVehiculeRepository vehiculeRepository;
    @Autowired IEquipementRepository equipementRepository;
    @Autowired IMaintenanceRepository maintenanceRepository;
    @Autowired IReservationRepository reservationRepository;

    @Test
    void contextLoads() {
    }

    // ================= Partie 2 : Agence =================
    @Test
    void testAgenceRepository() {
        Agence a = new Agence();
        a.setNom("Agence Tunis Centre");          // adapte aux attributs réels de Agence

        Agence saved = agenceRepository.save(a);
        System.out.println("Agence sauvegardée : " + saved);
        assertNotNull(saved.getId());              // ou getIdAgence()

        List<Agence> all = agenceRepository.findAll();
        System.out.println("Toutes les agences : " + all);
        assertFalse(all.isEmpty());

        Optional<Agence> found = agenceRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        System.out.println("findById : " + found.get());

        assertTrue(agenceRepository.existsById(saved.getId()));
        System.out.println("Nombre d'agences : " + agenceRepository.count());

        //agenceRepository.deleteById(saved.getId());
        //assertFalse(agenceRepository.existsById(saved.getId()));
    }

    // ================= Partie 3 : Client =================
    @Test
    void testClientRepository() {
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami@mail.com");
        c1.setTelephone("20123456");
        c1.setNumPermis("P123456");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Amel");
        c2.setEmail("amel@mail.com");
        c2.setTelephone("98123456");
        c2.setNumPermis("P654321");
        c2.setDateInscription(LocalDate.now());

        Client s1 = clientRepository.save(c1);
        Client s2 = clientRepository.save(c2);
        assertNotNull(s1.getId());
        assertNotNull(s2.getId());

        System.out.println("Clients : " + clientRepository.findAll());
        assertTrue(clientRepository.findById(s1.getId()).isPresent());

        assertTrue(clientRepository.existsById(s1.getId()));
        assertFalse(clientRepository.existsById(999999L));

        long total = clientRepository.count();
        System.out.println("Nombre de clients : " + total);
        assertTrue(total >= 2);

        //clientRepository.deleteById(s1.getId());
        //clientRepository.deleteById(s2.getId());
        //assertFalse(clientRepository.existsById(s1.getId()));
    }

    // ================= Partie 4 : Vehicule =================
    @Test
    void testVehiculeRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setTarifJournalier(new BigDecimal("90.00"));

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());

        System.out.println("Véhicules : " + vehiculeRepository.findAll());
        long avant = vehiculeRepository.count();
        System.out.println("Nombre de véhicules : " + avant);

        // modification : save() fait un UPDATE car l'id existe déjà
        saved.setTarifJournalier(new BigDecimal("110.00"));
        Vehicule modifie = vehiculeRepository.save(saved);
        assertEquals(0, new BigDecimal("110.00").compareTo(modifie.getTarifJournalier()));
        assertEquals(avant, vehiculeRepository.count());

        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
    }

    // ================= Partie 5 : exercice autonome =================

    // 1) Equipement
    @Test
    void testEquipementRepository() {
        Equipement e = new Equipement();
        e.setLibelle("GPS");

        Equipement saved = equipementRepository.save(e);
        assertNotNull(saved.getId());
        System.out.println("Equipement : " + saved);

        assertFalse(equipementRepository.findAll().isEmpty());
        assertTrue(equipementRepository.findById(saved.getId()).isPresent());
        assertTrue(equipementRepository.existsById(saved.getId()));

        equipementRepository.deleteById(saved.getId());
        assertFalse(equipementRepository.existsById(saved.getId()));
    }

    // 2) Maintenance (nécessite un véhicule existant)
    @Test
    void testMaintenanceRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("456 TUN 7890");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setTarifJournalier(new BigDecimal("80.00"));
        v = vehiculeRepository.save(v);

        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange");
        m.setVehicule(v);

        Maintenance saved = maintenanceRepository.save(m);
        assertNotNull(saved.getId());
        System.out.println("Maintenance : " + saved);

        assertFalse(maintenanceRepository.findAll().isEmpty());
        assertTrue(maintenanceRepository.existsById(saved.getId()));

        maintenanceRepository.deleteById(saved.getId());
        assertFalse(maintenanceRepository.existsById(saved.getId()));

        vehiculeRepository.deleteById(v.getIdVehicule());   // nettoyage
    }

    // 3) Reservation (nécessite un client et un véhicule)
    @Test
    void testReservationRepository() {
        Client c = new Client();
        c.setNom("Gharbi");
        c.setPrenom("Ines");
        c.setEmail("ines@mail.com");
        c.setTelephone("55123456");
        c.setNumPermis("P111222");
        c.setDateInscription(LocalDate.now());
        c = clientRepository.save(c);

        Vehicule v = new Vehicule();
        v.setImmatriculation("789 TUN 1234");
        v.setMarque("Kia");
        v.setModele("Picanto");
        v.setTarifJournalier(new BigDecimal("70.00"));
        v = vehiculeRepository.save(v);

        Reservation r = new Reservation();
        r.setDateDebut(LocalDate.now());
        r.setDateFin(LocalDate.now().plusDays(3));
        r.setStatut(StatutReservation.EN_ATTENTE);   // adapte au nom réel de ton enum
        r.setClient(c);
        r.setVehicule(v);

        Reservation saved = reservationRepository.save(r);
        assertNotNull(saved.getId());
        System.out.println("Reservation : " + saved);

        assertTrue(reservationRepository.findById(saved.getId()).isPresent());
        assertTrue(reservationRepository.existsById(saved.getId()));

        reservationRepository.deleteById(saved.getId());
        assertFalse(reservationRepository.existsById(saved.getId()));

        // nettoyage dans l'ordre inverse des dépendances
        clientRepository.deleteById(c.getId());
        vehiculeRepository.deleteById(v.getIdVehicule());
    }
}