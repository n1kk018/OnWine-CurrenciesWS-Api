package fr.afcepf.atod.ws.currency.biz.test;
import java.text.ParseException;

import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.afcepf.atod.ws.currency.biz.api.ICurrencyConverter;
import fr.afcepf.atod.ws.currency.biz.impl.CurrencyConverter;
import fr.afcepf.atod.ws.currency.dao.api.ICurrencyDao;
import fr.afcepf.atod.ws.currency.entity.Currency;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSError;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSException;
/**
 * Test class for the business soap.
 * @author nikko
 *
 */
public class TestCurrencyConverter {
    /**
     * persistence layer mock for {@link Currency}.
     */
    private static ICurrencyDao mockCurrencyDao;
    /**
     * taux de change Livre sterling par rapport au Dollar.
     */
    private static final Double POUNDEXCHANGERATE = 0.813088;
    /**
     * taux de change Euro par rapport au Dollar.
     */
    private static final Double EUROEXCHANGERATE = 0.9106;
    /**
     * Currency GBP.
     */
    private static Currency currencyGBP = new Currency(1, "livre", "GBP", POUNDEXCHANGERATE);
    /**
     * Currency EUR.
     */
    private static Currency currencyEUR = new Currency(2, "euro", "EUR", EUROEXCHANGERATE);
    /**
     * Entry amount.
     */
    private static final Double ENTRY_AMOUNT = 10D;
    /**
     * Expected amount.
     */
    private static final Double EXPECTED_AMOUNT = 8.926741696404063;
    /**
     * Api where is located the service.
     */
    private static ICurrencyConverter bu = new CurrencyConverter();
    /**
     * Exception attendue dans l'échec.
     */
    private static CurrenciesWSError expectedEchec = CurrenciesWSError.RECHERCHE_NON_PRESENTE_EN_BASE;
    /**
     * Before all tests.
     * @throws CurrenciesWSException pour le mock.
     * @throws ParseException pour le mock.
     */
    @BeforeClass
    public static void beforeAllTests() throws CurrenciesWSException, ParseException {
        mockCurrencyDao = EasyMock.createMock(ICurrencyDao.class);
        //EasyMock.expect(mockCurrencyDao.findByCode("CNY")).andReturn(null);
        EasyMock.expect(mockCurrencyDao.findByCode("GBP")).andReturn(currencyGBP);
        EasyMock.expect(mockCurrencyDao.findByCode("EUR")).andReturn(currencyEUR);
        EasyMock.replay(mockCurrencyDao);
        ((CurrencyConverter) bu).setSrcCurrency(currencyEUR);
        ((CurrencyConverter) bu).setTrgtCurrency(currencyGBP);
    }
    /**
     * Aprés tous les tests.
     */
    @AfterClass
    public static void afterAllTests() {
        EasyMock.verify(mockCurrencyDao);
    }
    /**
     * Test du cas ou une conversion est réussie.
     * @throws CurrenciesWSException caca.
     */
    @Test
    public void successTest() throws CurrenciesWSException {
        Double amount = bu.convert(ENTRY_AMOUNT, "EUR", "GBP");
        Assert.assertNotNull(amount);
        Assert.assertEquals(EXPECTED_AMOUNT, amount);
    }
    /**
     * Test du cas ou une conversion échoue.
     * @throws CurrenciesWSException caca.
     */
    //@Test
    public void testEchec() {
        try {
            bu.convert(ENTRY_AMOUNT, "CNY", "GBP");
            Assert.fail("conversion effectuée... pas normal!");
        } catch (CurrenciesWSException paramE) {
            Assert.assertEquals(expectedEchec, paramE.getWsError());
        }
    }

}
