package com.luan.common.service;

import com.luan.common.annotation.AuditFieldLabel;
import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparison;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Classe abstrata para operações de revisão/auditoria em entidades.
 *
 * @param <T> Tipo da entidade que estende BaseEntity
 * @param <ID> Tipo do identificador da entidade
 */
public abstract class RevisionService<T extends BaseEntity, ID> {
    
    /**
     * Deve retornar a classe da entidade
     */
    protected abstract Class<T> getEntityClass();
    
    /**
     * Deve retornar o EntityManager para operações com o banco de dados
     */
    protected abstract EntityManager getEntityManager();
    
    /**
     * Recupera todas as revisões de uma entidade específica.
     *
     * @param entityId O ID da entidade
     * @return Lista de revisões da entidade
     */
    public List<Revision<T>> findAllRevisions(ID entityId) {
        AuditReader reader = AuditReaderFactory.get(getEntityManager());
        List<Number> revisionsNumbers = reader.getRevisions(getEntityClass(), entityId);
        List<Revision<T>> revisionList = new ArrayList<>();
        for (Number revisionNumber : revisionsNumbers) {
            T entity = reader.find(getEntityClass(), entityId, revisionNumber.intValue());
            Revision<T> revision = new Revision<>();
            revision.setId(revisionNumber);
            revision.setType(getRevisionType(entityId, revisionNumber));
            revision.setEntity(entity);
            revision.setDate(reader.getRevisionDate(revisionNumber));
            AuditRevisionEntity auditRevisionEntity = reader.findRevision(AuditRevisionEntity.class, revisionNumber);
            revision.setUser(auditRevisionEntity.getUser());
            revisionList.add(revision);
        }
        return revisionList;
    }
    
    /**
     * Compara uma revisão específica com a revisão anterior.
     *
     * @param entityId O ID da entidade
     * @param revisionId O ID da revisão a ser comparada
     * @return Um objeto contendo a comparação entre as revisões
     */
    public RevisionComparison<T> compareWithPreviousRevision(ID entityId, Integer revisionId) {
        AuditReader auditReader = AuditReaderFactory.get(getEntityManager());

        // Obtém todas as revisões para a entidade
        List<Number> revisions = auditReader.getRevisions(getEntityClass(), entityId);
        int currentRevisionIndex = revisions.indexOf(revisionId);

        if (currentRevisionIndex == -1) {
            throw new IllegalArgumentException("Revision not found for the entity");
        }

        // Obtém a revisão anterior, se existir
        Integer previousRevisionId = currentRevisionIndex > 0
                ? revisions.get(currentRevisionIndex - 1).intValue()
                : null;

        // Carrega as entidades das revisões
        T currentEntity = auditReader.find(getEntityClass(), entityId, revisionId);
        Object previousEntity = previousRevisionId != null
                ? auditReader.find(getEntityClass(), entityId, previousRevisionId)
                : null;

        // Compara as entidades para detectar alterações
        List<FieldChange> fieldChanges = compareEntities(previousEntity, currentEntity);

        // Obtém metadados da revisão atual
        AuditRevisionEntity auditRevisionEntity = auditReader.findRevision(AuditRevisionEntity.class, revisionId);
        RevisionType revisionType = getRevisionType(entityId, revisionId);

        Revision<T> revision = new Revision<>();
        revision.setId(revisionId);
        revision.setType(revisionType);
        revision.setEntity(currentEntity);
        revision.setDate(auditRevisionEntity.getRevisionDate());
        revision.setUser(auditRevisionEntity.getUser());

        return new RevisionComparison<T>(revision, fieldChanges);
    }
    
    /**
     * Recupera todas as comparações de revisões para uma entidade específica.
     *
     * @param entityId O ID da entidade
     * @return Lista de comparações entre revisões, ordenadas da mais recente para a mais antiga
     */
    public List<RevisionComparison<T>> findAllRevisionsComparisons(ID entityId) {
        AuditReader auditReader = AuditReaderFactory.get(getEntityManager());
        List<Number> revisionsNumbers = auditReader.getRevisions(getEntityClass(), entityId);

        List<RevisionComparison<T>> comparisons = new ArrayList<>();
        for (Number revisionNumber : revisionsNumbers) {
            RevisionComparison<T> comparison = compareWithPreviousRevision(entityId, revisionNumber.intValue());
            comparisons.add(comparison);
        }

        // Ordena as comparações com base na data da revisão mais recente para a mais antiga
        comparisons.sort(Comparator.comparing(
                RevisionComparison::getRevision,
                Comparator.comparing(Revision::getDate, Comparator.reverseOrder())
        ));

        return comparisons;
    }
    
    /**
     * Obtém o tipo de revisão para uma entidade e revisão específicas.
     *
     * @param entityId O ID da entidade
     * @param revisionId O ID da revisão
     * @return O tipo de revisão (ADD, MOD, DEL)
     */
    public RevisionType getRevisionType(ID entityId, Number revisionId) {
        AuditReader auditReader = AuditReaderFactory.get(getEntityManager());
        List<Object[]> revisionData = auditReader.createQuery()
                .forRevisionsOfEntity(getEntityClass(), false, true)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionNumber().eq(revisionId))
                .getResultList();

        if (!revisionData.isEmpty()) {
            return (RevisionType) revisionData.getFirst()[2];
        }
        return RevisionType.ADD;
    }
    
    /**
     * Compara duas entidades e retorna as alterações entre elas.
     */
    protected List<FieldChange> compareEntities(Object oldEntity, Object newEntity) {
        List<FieldChange> changes = new ArrayList<>();

        if (oldEntity == null) {
            return new ArrayList<>();
        }

        if (newEntity == null) return changes;

        Class<?> clazz = oldEntity.getClass();
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getName().startsWith("$$_") ||
                field.getName().contains("hibernate") ||
                field.getName().equals("serialVersionUID") ||
                field.getName().equals("id") ||
                field.getDeclaringClass().getName().startsWith("java.")) {
                continue;
            }

            AuditFieldLabel annotation = field.getAnnotation(AuditFieldLabel.class);
            String label = field.getName();
            int order = 0;

            if (annotation != null) {
                // Se o campo deve ser ignorado, pula para o próximo
                if (annotation.ignore()) continue;

                label = annotation.value();
                order = annotation.order();
            }

            try {
                Object oldValue = field.get(oldEntity);
                Object newValue = field.get(newEntity);

                if (!Objects.equals(oldValue, newValue)) {
                    FieldChange change = new FieldChange();
                    change.setName(field.getName());
                    change.setLabel(label);

                    // Define a ordenação
                    if (order == 0) {
                        order = changes.size() + 1; // Ordem baseada na posição no fluxo
                    }
                    change.setOrder(order);

                    change.setOldValue(oldValue);
                    change.setNewValue(newValue);

                    // Comparação recursiva para subobjetos
                    if (isComplex(field.getType())) {
                        List<FieldChange> subChanges = compareEntities(oldValue, newValue);
                        change.setSubFieldChanges(subChanges);
                    }

                    changes.add(change);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error comparing fields", e);
            }
        }

        // Ordena as mudanças com base na ordem definida
        changes.sort(Comparator.comparingInt(FieldChange::getOrder));
        return changes;
    }

    /**
     * Obtém todos os campos de uma classe e suas superclasses.
     */
    protected List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * Verifica se um tipo é complexo (não é primitivo, enum ou tipo básico).
     */
    protected boolean isComplex(Class<?> type) {
        return !type.isPrimitive() &&
               !type.isEnum() &&
               !CharSequence.class.isAssignableFrom(type) &&
               !Number.class.isAssignableFrom(type) &&
               !Date.class.isAssignableFrom(type) &&
               !type.equals(java.util.UUID.class);
    }
} 