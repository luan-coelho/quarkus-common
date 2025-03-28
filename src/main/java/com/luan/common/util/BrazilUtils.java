package com.luan.common.util;

import java.util.regex.Pattern;

public class BrazilUtils {

    public static String addCpfMask(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido");
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public static String removeCpfMask(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo");
        }
        return cpf.replaceAll("[^\\d]", "");
    }

    public static boolean validateCep(String cep) {
        if (cep == null) {
            return false;
        }
        return Pattern.matches("\\d{5}-?\\d{3}", cep);
    }

    public static String addCepMask(String cep) {
        if (cep == null || cep.length() != 8 || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido");
        }
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }

    public static String removeCepMask(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("CEP não pode ser nulo");
        }
        return cep.replaceAll("[^\\d]", "");
    }

    public static String addCnpjMask(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/"
                + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }

    public static String removeCnpjMask(String cnpj) {
        if (cnpj == null) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo");
        }
        return cnpj.replaceAll("[^\\d]", "");
    }
    
}