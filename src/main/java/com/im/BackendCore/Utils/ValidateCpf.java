package com.im.BackendCore.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateCpf {
    private static final Logger logger = LoggerFactory.getLogger(ValidateCpf.class);

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            logger.warn("CPF validation failed: CPF is null");
            return false;
        }

        try {
            // Remove dots and hyphens
            cpf = cpf.replaceAll("\\D", ""); // Remove all non-digit characters

            if (cpf.length() != 11) {
                logger.warn("CPF validation failed: CPF length is not 11");
                return false;
            }

            // Check if all digits are the same (this is invalid)
            if (cpf.matches("(\\d)\\1{10}")) {
                logger.warn("CPF validation failed: All digits are the same");
                return false;
            }

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            firstDigit = (firstDigit >= 10) ? 0 : firstDigit;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            secondDigit = (secondDigit >= 10) ? 0 : secondDigit;

            boolean isValid = firstDigit == Character.getNumericValue(cpf.charAt(9)) &&
                    secondDigit == Character.getNumericValue(cpf.charAt(10));

            if (!isValid) {
                logger.warn("CPF validation failed: Verification digits don't match");
            }

            return isValid;
        } catch (NumberFormatException e) {
            logger.error("CPF validation error: Number format exception", e);
            return false;
        } catch (Exception e) {
            logger.error("CPF validation error: Unexpected exception", e);
            return false;
        }
    }
}