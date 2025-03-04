package com.luan.common.util;

import jakarta.validation.*;
import jakarta.validation.metadata.ConstraintDescriptor;

import java.util.*;
import java.util.function.Consumer;

public class ValidationUtil {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     * Valida um objeto e lança exceção caso haja violações.
     *
     * @param object Objeto a ser validado
     * @param <T>    Tipo do objeto
     */
    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }

    /**
     * Valida um objeto e permite adicionar violações manuais antes de lançar exceção.
     *
     * @param object               Objeto a ser validado
     * @param additionalViolations Conjunto de violações adicionais
     * @param <T>                  Tipo do objeto
     */
    public static <T> void validateWithCustomViolations(T object, Set<ConstraintViolation<?>> additionalViolations) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        Set<ConstraintViolation<?>> allViolations = new HashSet<>(violations);
        allViolations.addAll(additionalViolations);

        if (!allViolations.isEmpty()) {
            throw new ConstraintViolationException(allViolations);
        }
    }

    /**
     * Cria uma violação manualmente.
     *
     * @param field   Campo que está inválido
     * @param message Mensagem de erro
     * @param <T>     Tipo do objeto
     * @return ConstraintViolation<T> personalizado
     */
    public static <T> ConstraintViolation<T> createViolation(String field, String message) {
        return new ConstraintViolation<>() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public String getMessageTemplate() {
                return message;
            }

            @Override
            public T getRootBean() {
                return null;
            }

            @Override
            public Class<T> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }


            @Override
            public Path getPropertyPath() {
                return new Path() {

                    @Override
                    public Iterator<Node> iterator() {
                        return Collections.emptyIterator();
                    }

                    @Override
                    public void forEach(Consumer<? super Node> action) {
                        Path.super.forEach(action);
                    }

                    @Override
                    public Spliterator<Node> spliterator() {
                        return Path.super.spliterator();
                    }

                    @Override
                    public String toString() {
                        return field;
                    }
                };
            }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };

    }

}
