package com.technicalsupport.domainmodel.valueobjects;

public class DocumentValidator {
    public static boolean cpfIsValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("[0-9]{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        int ponderosity = 10;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * ponderosity--;
        }

        int digit1 = 11 - (sum % 11);
        if (digit1 == 10 || digit1 == 11) digit1 = 0;

        sum = 0;
        ponderosity = 11;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * ponderosity--;
        }

        int digit2 = 11 - (sum % 11);
        if (digit2 == 10 || digit2 == 11) digit2 = 0;

        return digit1 == Character.getNumericValue(cpf.charAt(9)) && digit2 == Character.getNumericValue(cpf.charAt(10));
    }
}
