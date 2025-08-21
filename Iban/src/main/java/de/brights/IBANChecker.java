package de.brights;

import java.util.*;

/**
 * Die Klasse {@code IBANChecker} bietet Funktionalität zur Validierung von IBANs
 * nach dem international standardisierten Verfahren (ISO 13616).
 *
 * Unterstützte Länder:
 * <ul>
 *     <li>AT</li>
 *     <li>BE</li>
 *     <li>CZ</li>
 *     <li>DE</li>
 *     <li>DK</li>
 *     <li>FR</li>
 * </ul>
 *
 * Die Prüfung basiert auf:
 * <ul>
 *     <li>Prüfung der Länge anhand des Ländercodes</li>
 *     <li>Umsortieren der IBAN</li>
 *     <li>Umwandlung in numerische Form</li>
 *     <li>Modulo-97-Prüfung</li>
 * </ul>
 *
 * @author Marcel
 */
public class IBANChecker {

    /**
     * Eine Map mit bekannten Ländercodes und ihrer erwarteten IBAN-Länge.
     */
    private static final Map<String, Integer> chars = new HashMap<>();

    // Initialisierung der gültigen IBAN-Längen pro Land
    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    /**
     * Hauptmethode für den manuellen Konsolentest.
     *
     * @param args wird nicht verwendet
     */
    public static void main(String[] args) {
        String iban = "DE89370400440532013000";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + (validate(iban) ? "valid" : "invalid"));
    }

    /**
     * Validiert eine gegebene IBAN.
     *
     * @param iban Die zu prüfende IBAN als String
     * @return {@code true}, wenn die IBAN gültig ist, sonst {@code false}
     */
    public static boolean validate(String iban) {
        if (!checkLength(iban)) return false;
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }

    /**
     * Prüft, ob die IBAN die korrekte Länge für ihr Herkunftsland hat.
     *
     * @param iban Die IBAN
     * @return {@code true}, wenn die Länge korrekt ist, sonst {@code false}
     */
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) == iban.length();
    }

    /**
     * Rearranged die IBAN, indem die ersten 4 Zeichen ans Ende verschoben werden.
     *
     * @param iban Die ursprüngliche IBAN
     * @return Die umsortierte IBAN
     */
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }

    /**
     * Wandelt eine alphanumerische IBAN in eine rein numerische Darstellung um.
     * Buchstaben werden nach dem Schema A=10 bis Z=35 ersetzt.
     *
     * @param iban Die umzuwandelnde IBAN
     * @return Die numerische Darstellung der IBAN
     * @throws IllegalArgumentException wenn ein ungültiges Zeichen enthalten ist
     */
    private static String convertToInteger(String iban) {
        StringBuilder result = new StringBuilder();
        for (char c : iban.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                result.append((int)(c - 'A' + 10));
            } else if (Character.isDigit(c)) {
                result.append(c);
            } else {
                throw new IllegalArgumentException("Ungültiges Zeichen in IBAN: " + c);
            }
        }
        return result.toString();
    }

    /**
     * Teilt den langen numerischen IBAN-String in Segmente von max. 9 Ziffern auf.
     *
     * @param iban Der numerische IBAN-String
     * @return Liste von String-Segmenten
     */
    private static List<String> createSegments(String iban) {
        List<String> segments = new ArrayList<>();
        while (!iban.isEmpty()) {
            int len = Math.min(9, iban.length());
            segments.add(iban.substring(0, len));
            iban = iban.substring(len);
        }
        return segments;
    }

    /**
     * Führt die Modulo-97-Prüfung durch.
     * Diese Prüfung ist der Kern der IBAN-Validierung.
     *
     * @param segments Liste der aufgeteilten numerischen IBAN-Segmente
     * @return Der Rest der Division durch 97 (gültig, wenn {@code == 1})
     */
    private static int calculate(List<String> segments) {
        long n = 0;
        for (String segment : segments) {
            n = Long.parseLong(n + segment) % 97;
        }
        return (int) n;
    }
}
