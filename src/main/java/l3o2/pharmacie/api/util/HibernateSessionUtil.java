package l3o2.pharmacie.api.util;

import jakarta.persistence.EntityManager;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire pour gérer les sessions Hibernate, notamment lors des opérations d'importation de données volumineuses.
 * Permet de contrôler le mode de flush pour optimiser les performances.
 */
public class HibernateSessionUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateSessionUtil.class);
    
    /**
     * Désactive temporairement le mode de flush automatique de la session Hibernate.
     * Cela peut améliorer les performances lors de l'insertion ou de la mise à jour de nombreuses entités.
     * @param entityManager L'EntityManager à configurer.
     * @return {@code true} si le mode de flush précédent était AUTO, {@code false} sinon.
     */
    public static boolean disableAutoFlush(EntityManager entityManager) {
        boolean previousAutoFlushMode = entityManager.unwrap(Session.class).getHibernateFlushMode() == FlushMode.AUTO;
        if (previousAutoFlushMode) {
            entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.MANUAL);
            log.debug("Mode de flush automatique désactivé");
        }
        return previousAutoFlushMode;
    }
    
    /**
     * Restaure le mode de flush automatique de la session Hibernate si celui-ci avait été désactivé.
     * @param entityManager L'EntityManager à configurer.
     * @param previousAutoFlushMode L'état précédent du mode de flush (généralement retourné par {@link #disableAutoFlush}).
     */
    public static void restoreAutoFlush(EntityManager entityManager, boolean previousAutoFlushMode) {
        if (previousAutoFlushMode) {
            entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.AUTO);
            log.debug("Mode de flush automatique restauré");
        }
    }
    
    /**
     * Effectue un flush explicite de l'EntityManager suivi d'un clear.
     * Le flush synchronise le contexte de persistance avec la base de données,
     * et le clear détache toutes les entités gérées, libérant de la mémoire.
     * @param entityManager L'EntityManager à nettoyer.
     */
    public static void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}
