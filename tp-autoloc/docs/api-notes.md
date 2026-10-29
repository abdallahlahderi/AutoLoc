# Atelier 5 — Spring MVC, Postman et SpringDoc

Application : `http://localhost:8081/api` (port 8081, préfixe `/api` défini par `server.servlet.context-path`).
Swagger UI : `http://localhost:8081/api/swagger-ui.html` — OpenAPI JSON : `http://localhost:8081/api/v3/api-docs`.

## A. Endpoints

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
|---|---|---|---|---|
| ContratController | GET /api/contrats | findAll() | 200 | — |
| ContratController | GET /api/contrats/{id} | findById(id) | 200 | 404 |
| ContratController | POST /api/contrats | create(contrat) | 201 | 400 |
| ContratController | PUT /api/contrats/{id} | update(id, contrat) | 200 | 404, 400 |
| ContratController | DELETE /api/contrats/{id} | deleteById(id) | 204 | 404 |
| PaiementController | GET /api/paiements | findAll() | 200 | — |
| PaiementController | GET /api/paiements/{id} | findById(id) | 200 | 404 |
| PaiementController | POST/PUT/DELETE /api/paiements | — (non exposé) | — | 405 |
| AgenceController | GET /api/agences | findAll() | 200 | — |
| AgenceController | GET /api/agences/{id} | findById(id) | 200 | 404 |
| AgenceController | POST /api/agences | create(agence) | 201 | 400 |
| AgenceController | PUT /api/agences/{id} | update(id, agence) | 200 | 404, 400 |
| AgenceController | DELETE /api/agences/{id} | deleteById(id) | 204 | 404 |
| ClientController | GET /api/clients | findAll() | 200 | — |
| ClientController | GET /api/clients/{id} | findById(id) | 200 | 404 |
| ClientController | POST /api/clients | create(client) | 201 | 400 |
| ClientController | PUT /api/clients/{id} | update(id, client) | 200 | 404, 400 |
| ClientController | DELETE /api/clients/{id} | deleteById(id) | 204 | 404 |
| EmployeController | GET /api/employes | findAll() | 200 | — |
| EmployeController | GET /api/employes/{id} | findById(id) | 200 | 404 |
| EmployeController | POST /api/employes | create(employe) | 201 | 400 |
| EmployeController | PUT /api/employes/{id} | update(id, employe) | 200 | 404, 400 |
| EmployeController | DELETE /api/employes/{id} | deleteById(id) | 204 | 404 |
| VehiculeController | GET /api/vehicules | findAll() | 200 | — |
| VehiculeController | GET /api/vehicules/{id} | findById(id) | 200 | 404 |
| VehiculeController | POST /api/vehicules | create(vehicule) | 201 | 400 |
| VehiculeController | PUT /api/vehicules/{id} | update(id, vehicule) | 200 | 404, 400 |
| VehiculeController | DELETE /api/vehicules/{id} | deleteById(id) | 204 | 404 |
| EquipementController | GET /api/equipements | findAll() | 200 | — |
| EquipementController | GET /api/equipements/{id} | findById(id) | 200 | 404 |
| EquipementController | POST /api/equipements | create(equipement) | 201 | 400 |
| EquipementController | PUT /api/equipements/{id} | update(id, equipement) | 200 | 404, 400 |
| EquipementController | DELETE /api/equipements/{id} | deleteById(id) | 204 | 404 |
| ReservationController | GET /api/reservations | findAll() | 200 | — |
| ReservationController | GET /api/reservations/{id} | findById(id) | 200 | 404 |
| ReservationController | POST /api/reservations | create(reservation) | 201 | 400 |
| ReservationController | PUT /api/reservations/{id} | update(id, reservation) | 200 | 404, 400 |
| ReservationController | DELETE /api/reservations/{id} | deleteById(id) | 204 | 404 |
| MaintenanceController | GET /api/maintenances | findAll() | 200 | — |
| MaintenanceController | GET /api/maintenances/{id} | findById(id) | 200 | 404 |
| MaintenanceController | POST /api/maintenances | create(maintenance) | 201 | 400 |
| MaintenanceController | PUT /api/maintenances/{id} | update(id, maintenance) | 200 | 404, 400 |
| MaintenanceController | DELETE /api/maintenances/{id} | deleteById(id) | 204 | 404 |

Les 404 viennent de `ResourceNotFoundException` et les 400 de `IllegalArgumentException`, traduites par `GlobalExceptionHandler` en JSON `{statut, message, horodatage}`. Le 405 est produit par Spring MVC (aucune méthode associée au verbe).

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
|---|---|---|
| Contrat.paiements / Paiement.contrat | `@JsonManagedReference("contrat-paiements")` / `@JsonBackReference("contrat-paiements")` | Composition : les paiements sont créés dans le JSON du contrat (cascade ALL). Jackson sérialise la liste et renseigne automatiquement `paiement.contrat` à la lecture. |
| Reservation.contrat (inverse de Contrat.reservation) | `@JsonIgnore` | Côté inverse (`mappedBy`) : coupe la boucle Contrat → Reservation → Contrat. |
| Agence.employes, Agence.vehicules | `@JsonIgnore` | Côtés inverses ; l'enfant désigne son agence par `{"agence": {"id": 1}}`. |
| Client.reservations | `@JsonIgnore` | Côté inverse ; la réservation désigne son client par son id. |
| Vehicule.reservations, Vehicule.maintenances | `@JsonIgnore` | Côtés inverses ; Reservation et Maintenance désignent leur véhicule par `{"vehicule": {"idVehicule": 1}}`. |
| Equipement.vehicules (inverse du @ManyToMany) | `@JsonIgnore` | Le côté propriétaire `Vehicule.equipements` reste sérialisé. |

Les côtés propriétaires (`Employe.agence`, `Reservation.client`, `Reservation.vehicule`, `Maintenance.vehicule`, `Vehicule.agence`) ne reçoivent aucune annotation : ils doivent rester lisibles dans les POST.

**Différence `@JsonBackReference` / `@JsonIgnore`** : `@JsonBackReference` fonctionne en paire avec `@JsonManagedReference`. Le champ est masqué à l'écriture, mais à la lecture Jackson le renseigne automatiquement avec l'objet parent (chaque paiement reçoit son contrat). `@JsonIgnore` exclut simplement le champ, dans les deux sens, sans aucun lien reconstruit. Mis sur `Reservation.client`, `@JsonBackReference` ferait ignorer le client envoyé dans le POST, et la réservation serait créée sans client.

## C. Postman et Swagger

| Outil | Apport | Exemple rencontré |
|---|---|---|
| Postman | Tester : requêtes enregistrées et rejouables, variables d'environnement (`baseUrl`, `contratId`), assertions automatiques | Scénario de cascade : « Créer un contrat avec ses paiements » (201), « Lister les paiements » (2 paiements créés sans appel à /paiements), « Supprimer un contrat » (204), puis GET /paiements/1 → 404 (cascade REMOVE) |
| Swagger UI | Documenter et explorer : contrat OpenAPI généré à partir du code, toujours synchronisé, essai direct des endpoints | POST /contrats exécuté depuis Swagger : la commande curl générée et le code 201 ; le corps d'exemple montre tout le graphe de l'entité (réservation, client, véhicule, agence), ce qui illustre le couplage API/base |

Les deux outils sont complémentaires : Postman vérifie le comportement, Swagger décrit le contrat.

## D. Anomalies SonarQube for IDE

| Règle | Explication | Correction |
|---|---|---|
| java:S1710 | `@ApiResponses({...})` est un conteneur inutile : `@ApiResponse` est répétable | Annotations `@ApiResponse` écrites directement les unes sous les autres |
| java:S8688 | `LocalDateTime.now()` dépend du fuseau implicite du serveur | `LocalDateTime.now(ZoneId.of("Africa/Tunis"))` dans `GlobalExceptionHandler` |
| java:S4684 | Entité JPA reçue en `@RequestBody` : couplage API/base, champs internes modifiables par le client | Assumée provisoirement (choix du TP) ; corrigée par des DTO au Chapitre 10 |

## E. Questions de compréhension

1. **Composant qui reçoit les requêtes** : le `DispatcherServlet`, point d'entrée unique. Il interroge le `HandlerMapping`, qui retrouve la méthode à appeler à partir du verbe HTTP et de l'URL (`@RequestMapping` de la classe + `@GetMapping`, `@PostMapping`… de la méthode).
2. **Dépendre de `IContratService`** : le contrôleur reste mince et ne connaît que le contrat du service. Il ne contourne pas la logique métier (gardes, règles, exceptions) en appelant le repository, et ne dépend pas d'une implémentation concrète : `ContratServiceImpl` peut être remplacé ou simulé en test sans modifier le contrôleur.
3. **Codes HTTP** : 201 Created indique qu'une nouvelle ressource a été créée (son identifiant est dans le corps) ; 204 No Content indique un succès sans corps à renvoyer. Une ressource absente est une erreur du client (mauvais identifiant), d'où 404 ; un 500 signalerait une panne du serveur. Le service lève `ResourceNotFoundException`, traduite en 404 par le `GlobalExceptionHandler`.
4. **`PaiementController` en lecture seule** : un paiement n'existe qu'à l'intérieur d'un contrat (composition, cascade ALL, `orphanRemoval`). Les créations, modifications et suppressions passent par le contrat, ce qui garantit la cohérence. Un POST /api/paiements reçoit **405 Method Not Allowed**.
5. **Risques de l'exposition des entités** : couplage entre l'API et le schéma de la base (renommer une colonne casse l'API), champs internes visibles et modifiables (id, associations), boucles de sérialisation, proxies Hibernate LAZY, corps d'exemple Swagger énormes. `Contrat.paiements` utilise `@JsonManagedReference` car c'est une composition : on veut voir et créer les paiements dans le JSON du contrat. `Agence.vehicules` utilise `@JsonIgnore` car un véhicule est créé seul et désigne son agence par son id : la liste n'est pas utile dans la réponse de l'agence et provoquerait une boucle.