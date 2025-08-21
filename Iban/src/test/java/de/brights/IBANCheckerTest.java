package de.brights;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IBANCheckerTest {
    @Test
    public void testValidIban() {
        assertTrue(IBANChecker.validate("DE89370400440532013000"));
    }

    @Test
    public void testInvalidIban() {
        assertFalse(IBANChecker.validate("DE21790200760027913173"));
    }

    @Test
    public void testWrongLength() {
        assertFalse(IBANChecker.validate("DE227902007600279131"));
    }

    @Test
    public void testUnknownCountry() {
        assertFalse(IBANChecker.validate("XX22790200760027913168"));
    }
}

