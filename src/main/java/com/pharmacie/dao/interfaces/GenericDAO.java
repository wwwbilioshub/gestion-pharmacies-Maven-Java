package com.pharmacie.dao.interfaces;
import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD de base
 * @param <T> Type de l'entité
 * @param <ID> Type de l'identifiant
 */
public interface GenericDAO<T, ID> {
	/**
     * Créer une nouvelle entité
     * @param entity L'entité à créer
     * @return L'entité créée avec son ID généré
     */
    T create(T entity);
    
    /**
     * Mettre à jour une entité existante
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     */
    T update(T entity);
    
    /**
     * Supprimer une entité par son ID
     * @param id L'identifiant de l'entité
     * @return true si la suppression a réussi, false sinon
     */
    boolean delete(ID id);
    
    /**
     * Trouver une entité par son ID
     * @param id L'identifiant de l'entité
     * @return Optional contenant l'entité si trouvée, vide sinon
     */
    Optional<T> findById(ID id);
    
    /**
     * Récupérer toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();
    
    /**
     * Compter le nombre total d'entités
     * @return Nombre d'entités
     */
    long count();
    
    /**
     * Vérifier si une entité existe par son ID
     * @param id L'identifiant de l'entité
     * @return true si l'entité existe, false sinon
     */
    boolean exists(ID id);

}
