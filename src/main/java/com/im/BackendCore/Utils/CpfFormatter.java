package com.im.BackendCore.Utils;

public class CpfFormatter {


    public static String format(String cpf) {
        if (cpf == null) {
            return null;
        }

        // Remove any non-digit characters
        String cleanCpf = cpf.replaceAll("\\D", "");

        // Check if we have the correct number of digits
        if (cleanCpf.length() != 11) {
            return cleanCpf; // Return as is if not 11 digits
        }

        // Format as xxx.xxx.xxx-xx
        return cleanCpf.substring(0, 3) + "." +
                cleanCpf.substring(3, 6) + "." +
                cleanCpf.substring(6, 9) + "-" +
                cleanCpf.substring(9, 11);
    }
}