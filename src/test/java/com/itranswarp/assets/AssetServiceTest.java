package com.itranswarp.assets;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class AssetServiceTest {

    static final Long USER_A = Users.TRADER;
    static final Long USER_B = Users.TRADER + 1;

    @Test
    public void testAssetService() {
        AssetService assetService = new AssetService();
        assetService.tryTransfer(Transfer.AVAILABLE_TO_AVAILABLE, Users.DEBT, USER_A, "FIAT", bd("12345.67"), false);
        assertBDEquals(bd("12345.67"), assetService.getAsset(USER_A, "FIAT").getAvailable());
        assertBDEquals(bd("-12345.67"), assetService.getAsset(Users.DEBT, "FIAT").getAvailable());

        assetService.tryTransfer(Transfer.AVAILABLE_TO_AVAILABLE, Users.DEBT, USER_B, "FIAT", bd("45678.9"), false);
        assertBDEquals(bd("45678.9"), assetService.getAsset(USER_B, "FIAT").getAvailable());
        assertBDEquals(bd("-58024.57"), assetService.getAsset(Users.DEBT, "FIAT").getAvailable());

        assertFalse(assetService.tryFreeze(USER_A, "FIAT", bd("12345.68")));
        assertTrue(assetService.tryFreeze(USER_A, "FIAT", bd("1234.56")));
        assertBDEquals(bd("11111.11"), assetService.getAsset(USER_A, "FIAT").getAvailable());
        assertBDEquals(bd("1234.56"), assetService.getAsset(USER_A, "FIAT").getFrozen());

        assetService.tryTransfer(Transfer.AVAILABLE_TO_AVAILABLE, Users.DEBT, USER_B, "STOCK", bd("12.34"), false);
        assertBDEquals(bd("12.34"), assetService.getAsset(USER_B, "STOCK").getAvailable());
        assertBDEquals(bd("-12.34"), assetService.getAsset(Users.DEBT, "STOCK").getAvailable());

        assetService.debug();
    }

    static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    static void assertBDEquals(BigDecimal d1, BigDecimal d2) {
        assertEquals(d1.stripTrailingZeros(), d2.stripTrailingZeros());
    }
}
