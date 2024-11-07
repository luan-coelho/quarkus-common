package com.luan.common.util.pagination;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Classe utilitária para operações com data e hora.
 */
public class DateUtils {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Obter a data atual.
     *
     * @return a data atual
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Comparar dois objetos LocalDate para ver se são no mesmo dia.
     *
     * @param date1 a primeira data
     * @param date2 a segunda data
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }

    /**
     * Comparar dois objetos LocalDateTime ignorando a hora.
     *
     * @param dateTime1 a primeira data e hora
     * @param dateTime2 a segunda data e hora
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDayIgnoringTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    /**
     * Comparar dois objetos ZonedDateTime ignorando a hora e os fusos horários.
     *
     * @param zonedDateTime1 a primeira data e hora com fuso horário
     * @param zonedDateTime2 a segunda data e hora com fuso horário
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDayIgnoringTimeAndZone(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2) {
        return zonedDateTime1.toLocalDate().isEqual(zonedDateTime2.toLocalDate());
    }

    /**
     * Comparar um LocalDate com um LocalDateTime ignorando a hora.
     *
     * @param date     a data
     * @param dateTime a data e hora
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDayIgnoringTime(LocalDate date, LocalDateTime dateTime) {
        return date.isEqual(dateTime.toLocalDate());
    }

    /**
     * Comparar um LocalDate com um ZonedDateTime ignorando a hora e os fusos horários.
     *
     * @param date          a data
     * @param zonedDateTime a data e hora com fuso horário
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDayIgnoringTimeAndZone(LocalDate date, ZonedDateTime zonedDateTime) {
        return date.isEqual(zonedDateTime.toLocalDate());
    }

    /**
     * Comparar um LocalDateTime com um ZonedDateTime ignorando a hora e os fusos horários.
     *
     * @param dateTime      a data e hora
     * @param zonedDateTime a data e hora com fuso horário
     * @return true se as datas forem iguais, false caso contrário
     */
    public static boolean isSameDayIgnoringTimeAndZone(LocalDateTime dateTime, ZonedDateTime zonedDateTime) {
        return dateTime.toLocalDate().isEqual(zonedDateTime.toLocalDate());
    }

    /**
     * Obter a data e hora atual.
     *
     * @return a data e hora atual
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Formatar uma data para o padrão "yyyy-MM-dd".
     *
     * @param date a data a ser formatada
     * @return a string da data formatada
     */
    public static String format(LocalDate date) {
        return date.format(DEFAULT_FORMATTER);
    }

    /**
     * Analisar uma string no formato "yyyy-MM-dd" para um LocalDate.
     *
     * @param dateString a string da data a ser analisada
     * @return a data analisada
     */
    public static LocalDate parse(String dateString) {
        return LocalDate.parse(dateString, DEFAULT_FORMATTER);
    }

    /**
     * Adicionar dias a uma data.
     *
     * @param date a data à qual adicionar dias
     * @param days o número de dias a adicionar
     * @return a nova data
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    /**
     * Subtrair dias de uma data.
     *
     * @param date a data da qual subtrair dias
     * @param days o número de dias a subtrair
     * @return a nova data
     */
    public static LocalDate subtractDays(LocalDate date, long days) {
        return date.minusDays(days);
    }

    /**
     * Calcular a diferença em dias entre duas datas.
     *
     * @param start a data inicial
     * @param end   a data final
     * @return o número de dias entre as datas
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Verificar se uma data está no futuro.
     *
     * @param date a data a ser verificada
     * @return true se a data estiver no futuro, false caso contrário
     */
    public static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Verificar se uma data está no passado.
     *
     * @param date a data a ser verificada
     * @return true se a data estiver no passado, false caso contrário
     */
    public static boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Verificar se uma data está em um ano bissexto.
     *
     * @param date a data a ser verificada
     * @return true se a data estiver em um ano bissexto, false caso contrário
     */
    public static boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }

    /**
     * Obter o início do dia para uma data.
     *
     * @param date a data
     * @return o início do dia
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * Obter o final do dia para uma data.
     *
     * @param date a data
     * @return o final do dia
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * Converter um LocalDate para um LocalDateTime no horário especificado.
     *
     * @param date a data
     * @param time a hora
     * @return a data e hora combinadas
     */
    public static LocalDateTime toDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    /**
     * Formatar uma data com um padrão customizado.
     *
     * @param date    a data a ser formatada
     * @param pattern o padrão a ser usado
     * @return a string da data formatada
     */
    public static String formatWithPattern(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Calcular a idade com base em uma data de nascimento.
     *
     * @param birthDate a data de nascimento
     * @return a idade em anos
     */
    public static int calculateAge(LocalDate birthDate) {
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

}