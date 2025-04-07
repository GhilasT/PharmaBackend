package l3o2.pharmacie.api.util;

import jakarta.persistence.EntityManager;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire pour gérer les sessions Hibernate dans les opérations d'importation
 */
public class HibernateSessionUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateSessionUtil.class);
    
    /**
     * Désactive temporairement le flush automatique de la session Hibernate
     * @param entityManager EntityManager à configurer
     * @return État précédent du mode de flush (true si AUTO)
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
     * Restaure le mode de flush automatique si nécessaire
     * @param entityManager EntityManager à configurer
     * @param previousAutoFlushMode État précédent du mode de flush
     */
    public static void restoreAutoFlush(EntityManager entityManager, boolean previousAutoFlushMode) {
        if (previousAutoFlushMode) {
            entityManager.unwrap(Session.class).setHibernateFlushMode(FlushMode.AUTO);
            log.debug("Mode de flush automatique restauré");
        }
    }
    
    /**
     * Effectue un flush et clear de l'EntityManager
     * @param entityManager EntityManager à nettoyer
     */
    public static void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}
