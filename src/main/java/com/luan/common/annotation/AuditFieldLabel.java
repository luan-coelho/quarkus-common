package com.luan.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A anotação {@code AuditFieldLabel} é utilizada para configurar a exibição do rótulo (label) de um campo
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditFieldLabel {

    /**
     * Nome rótulo (label) que será exibido na auditoria.
     * Exemplo de uso:
     * {@code @AuditFieldLabel("Nome")}
     *
     * @return rótulo (label) do campo.
     */
    String value();

}