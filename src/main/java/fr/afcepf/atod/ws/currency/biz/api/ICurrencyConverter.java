package fr.afcepf.atod.ws.currency.biz.api;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.afcepf.atod.ws.currency.biz.rest.Wrapper;
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
     * a reference map of the css class needed to display the currencies.
     */
    
    static final HashMap<String, String> REFCURRENCYMAP = new HashMap<String, String>();
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
            @WebParam(name = "src") String srcCurrency,
            @WebParam(name = "trgt") String trgtCurrency)
                    throws CurrenciesWSException;
    /**
     * Conversion method between two currencies.
     * @param paramAmount amount in Double to convert
     * @param paramSrcCurrency source currency.
     * @param paramTrgtCurrency target currency.
     * @throws CurrenciesWSException custom exception
     * @return Double converted amount
     */
    Wrapper convertAndFormat(@WebParam(name = "amount") Double paramAmount,
            @WebParam(name = "src") String paramSrcCurrency,
            @WebParam(name = "trgt") String paramTrgtCurrency)
                    throws CurrenciesWSException;
}
