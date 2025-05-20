package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.Commande;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.repository.CommandeRepository;
import l3o2.pharmacie.api.repository.VenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service pour l'analyse des ventes et des commandes.
 * Fournit des méthodes pour agréger les données de ventes et de commandes
 * sur différentes périodes (jour, semaine, mois).
 */
@Service
@RequiredArgsConstructor
public class AnalyseVentesService {

    private final VenteRepository venteRepository;
    private final CommandeRepository commandeRepository;
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");

    /**
     * Récupère le nombre de ventes et de commandes par jour pour les 7 derniers jours.
     * Inclut également le chiffre d'affaires (CA) des ventes par jour.
     * Les clés du map sont formatées comme "vente-YYYY-MM-DD", "commande-YYYY-MM-DD", "vente-CA-YYYY-MM-DD".
     * 
     * @return Une map contenant le nombre de ventes, de commandes et le CA par jour.
     */
    public Map<String, Integer> getVentesParJourSemaine() {
        return getVentesEtCommandesParJour(7);
    }

    /**
     * Récupère le nombre de ventes et de commandes par jour pour les 30 derniers jours.
     * Inclut également le chiffre d'affaires (CA) des ventes par jour.
     * Les clés du map sont formatées comme "vente-YYYY-MM-DD", "commande-YYYY-MM-DD", "vente-CA-YYYY-MM-DD".
     * 
     * @return Une map contenant le nombre de ventes, de commandes et le CA par jour.
     * @throws RuntimeException si une erreur survient lors de la récupération des données.
     */
    public Map<String, Integer> getVentesParJourMois() {
        try {
            return getVentesEtCommandesParJour(30);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des données par jour", e);
        }
    }

    /**
     * Récupère le nombre de ventes, de commandes et le chiffre d'affaires (CA) des ventes par mois
     * pour un nombre de mois spécifié en remontant à partir de la date actuelle.
     * Les clés du map sont formatées comme "vente-YYYY-MM", "commande-YYYY-MM", "vente-CA-YYYY-MM".
     * 
     * @param nbMois Le nombre de mois pour lesquels récupérer les données.
     * @return Une map contenant le nombre de ventes, de commandes et le CA par mois.
     * @throws RuntimeException si une erreur survient lors de la récupération des données.
     */
    public Map<String, Integer> getVentesParMois(int nbMois) {
        try {
            Date endDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.MONTH, -nbMois + 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();

            // Ventes
            List<Vente> ventes = venteRepository.findByDateVenteBetween(startDate, endDate);
            Map<YearMonth, Long> venteCounts = ventes.stream()
                    .map(v -> YearMonth.from(convertToLocalDate(v.getDateVente())))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // Chiffre d'affaire des ventes
            Map<YearMonth, Double> venteRevenues = ventes.stream()
                    .collect(Collectors.groupingBy(
                            v -> YearMonth.from(convertToLocalDate(v.getDateVente())),
                            Collectors.summingDouble(Vente::getMontantTotal)
                    ));

            // Commandes
            List<Commande> commandes = commandeRepository.findByDateCommandeBetween(startDate, endDate);
            Map<YearMonth, Long> commandeCounts = commandes.stream()
                    .map(c -> YearMonth.from(convertToLocalDate(c.getDateCommande())))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Map<String, Integer> result = new LinkedHashMap<>();
            YearMonth currentYearMonth = YearMonth.from(convertToLocalDate(startDate));
            YearMonth endYearMonth = YearMonth.from(convertToLocalDate(endDate));

            while (!currentYearMonth.isAfter(endYearMonth)) {
                String venteKey = "vente-" + currentYearMonth;
                String commandeKey = "commande-" + currentYearMonth;
                String caVenteKey = "vente-CA-" + currentYearMonth;

                result.put(venteKey, venteCounts.getOrDefault(currentYearMonth, 0L).intValue());
                result.put(commandeKey, commandeCounts.getOrDefault(currentYearMonth, 0L).intValue());
                result.put(caVenteKey, venteRevenues.getOrDefault(currentYearMonth, 0.0).intValue()); // Ajout du CA

                currentYearMonth = currentYearMonth.plusMonths(1);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }

    /**
     * Récupère le nombre de ventes, de commandes et le chiffre d'affaires (CA) des ventes par jour
     * pour un nombre de jours spécifié en remontant à partir de la date actuelle.
     * Les clés du map sont formatées comme "vente-YYYY-MM-DD", "commande-YYYY-MM-DD", "vente-CA-YYYY-MM-DD".
     * 
     * @param nbJours Le nombre de jours pour lesquels récupérer les données.
     * @return Une map contenant le nombre de ventes, de commandes et le CA par jour.
     * @throws IllegalArgumentException si les dates de début et de fin sont invalides.
     * @throws RuntimeException si une erreur survient lors de la récupération des données.
     */
    private Map<String, Integer> getVentesEtCommandesParJour(int nbJours) {
        try {
            Date endDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -nbJours + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startDate = calendar.getTime();

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException("Dates invalides");
            }

            // Ventes
            List<Vente> ventes = venteRepository.findByDateVenteBetween(startDate, endDate);
            Map<LocalDate, Long> venteCounts = ventes.stream()
                    .map(v -> convertToLocalDate(v.getDateVente()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // Chiffre d'affaire des ventes
            Map<LocalDate, Double> venteRevenues = ventes.stream()
                    .collect(Collectors.groupingBy(
                            v -> convertToLocalDate(v.getDateVente()),
                            Collectors.summingDouble(Vente::getMontantTotal)
                    ));

            // Commandes
            List<Commande> commandes = commandeRepository.findByDateCommandeBetween(startDate, endDate);
            Map<LocalDate, Long> commandeCounts = commandes.stream()
                    .map(c -> convertToLocalDate(c.getDateCommande()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Map<String, Integer> result = new LinkedHashMap<>();
            LocalDate currentDate = convertToLocalDate(startDate);
            LocalDate endLocalDate = convertToLocalDate(endDate);

            while (!currentDate.isAfter(endLocalDate)) {
                String venteKey = "vente-" + currentDate;
                String commandeKey = "commande-" + currentDate;
                String caVenteKey = "vente-CA-" + currentDate;

                result.put(venteKey, venteCounts.getOrDefault(currentDate, 0L).intValue());
                result.put(commandeKey, commandeCounts.getOrDefault(currentDate, 0L).intValue());
                result.put(caVenteKey, venteRevenues.getOrDefault(currentDate, 0.0).intValue()); // Ajout du CA

                currentDate = currentDate.plusDays(1);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des données", e);
        }
    }

    /**
     * Convertit un objet {@link Date} en {@link LocalDate} en utilisant le fuseau horaire {@code Europe/Paris}.
     * 
     * @param date L'objet Date à convertir. Ne doit pas être null.
     * @return L'objet LocalDate correspondant.
     * @throws IllegalArgumentException si la date fournie est null.
     */
    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }
        return date.toInstant().atZone(ZONE_ID).toLocalDate();
    }
}
