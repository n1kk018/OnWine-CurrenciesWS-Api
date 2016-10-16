package fr.afcepf.atod.ws.currency.biz.api;

import java.util.List;

import javax.ejb.Remote;
import javax.jws.WebService;

import fr.afcepf.atod.ws.currency.dto.DTCurrency;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSException;
/**
 * API of the Currencies Converter WS.
 * @author nikko
 *
 */
@Remote
@WebService
public interface ICurrencyConverter {
    /**
     * Get the complete list of {@link DTCurrency} availables.
     * @throws CurrenciesWSException custom exception
     * @return List list of {@link DTCurrency}
     */
    List<DTCurrency> getAllCurrencies() throws CurrenciesWSException;
}
