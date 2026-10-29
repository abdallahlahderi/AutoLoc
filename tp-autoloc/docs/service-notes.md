## D. Anomalies SonarQube for IDE

| # | Règle | Explication | Correction |
|---|---|---|---|
| 1 | java:S6813 | Injection par attribut (`@Autowired` sur le champ) : dépendance cachée, champ non `final`, `null` hors conteneur. | Retour à l'injection par constructeur (`@RequiredArgsConstructor` + `private final`). |
| 2 | java:S1128 | Imports inutiles : `RequiredArgsConstructor` dans `ContratServiceImpl`, `CrudRepository` et `Contrat` dans les 9 repositories (17 occurrences). | Optimize Imports (Ctrl+Alt+O) sur `service` et `repository`. |
| 3 | java:S112 | `RuntimeException` générique dans `findById` : l'appelant ne peut pas distinguer l'erreur. | Exception métier `ResourceNotFoundException`. |
| 4 | java:S1598 | Le fichier `PaiementServiceImpl.java` ne correspondait pas au package déclaré. | Fichier renommé et classe placée dans `service/impl`. |

Après corrections, l'analyse des packages `service`, `repository` et `exception` ne signale plus aucune anomalie.