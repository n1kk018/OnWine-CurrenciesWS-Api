package fr.afcepf.atod.ws.currency.biz.api;

import java.util.List;

import javax.ejb.Remote;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.afcepf.atod.ws.currency.dto.DTCurrency;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSException;
/**
 * API of the Currencies Converter WS.
 * @author nikko
 *
 */
@Remote
@WebService(targetNamespace = "http://soap.currency.ws.atod.afcepf.fr/")
public interface ICurrencyConverter {
    /**
     * Get the complete list of {@link DTCurrency} availables.
     * @throws CurrenciesWSException custom exception
     * @return List list of {@link DTCurrency}
     */
    List<DTCurrency> getAllCurrencies() throws CurrenciesWSException;
    /**
     * Conversion method between two currencies.
     * @param amount amount in Double to convert
     * @param srcCurrency source currency.
     * @param trgtCurrency target currency.
     * @throws CurrenciesWSException custom exception
     * @return Double converted amount
     */
    Double convert(@WebParam(name = "amount") Double amount,
            @WebParam(name = "srcCurrency") String srcCurrency,
            @WebParam(name = "trgtCurrency") String trgtCurrency)
                    throws CurrenciesWSException;
}
