package fr.afcepf.atod.ws.currency.biz.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import fr.afcepf.atod.ws.currency.biz.api.ICurrencyConverter;
import fr.afcepf.atod.ws.currency.dao.api.ICurrencyDao;
import fr.afcepf.atod.ws.currency.dto.DTCurrency;
import fr.afcepf.atod.ws.currency.entity.Currency;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSException;
/**
 * Concrete Implementation of Currency converter WS.
 * @author nikko
 *
 */
@Stateless
@WebService(endpointInterface = "fr.afcepf.atod."
        + "ws.currency.biz.api.ICurrencyConverter",
        targetNamespace = "http://impl.biz.currency.ws.atod.afcepf.fr/")
public class CurrencyConverter implements ICurrencyConverter, Serializable {
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -3023413234131773165L;
    /**
     * injected currency dao.
     */
    @EJB
    private ICurrencyDao dao;
    @Override
    public List<DTCurrency> getAllCurrencies() throws CurrenciesWSException {
        List<DTCurrency> listDTO  = new ArrayList<DTCurrency>();
        for (Currency  c : dao.findAll()) {
            listDTO.add(entityDevise2DeviseDTO(c));
        }
        return listDTO;
    }
    /**
     * Utility method used for converting a DAO entity to a DTO.
     * @param c {@link Currency}
     * @return {@link DTCurrency}
     */
    private DTCurrency entityDevise2DeviseDTO(Currency c) {
        return new DTCurrency(c.getId(),
                c.getName(),
                c.getCode(),
                c.getRate());
    }
    @Override
    public Double convert(Double paramAmount,
            String paramSrcCurrency,
            String paramTrgtCurrency)
            throws CurrenciesWSException {
        Currency srcCurrency = dao.findByCode(paramSrcCurrency);
        Currency trgtCurrency = dao.findByCode(paramTrgtCurrency);
        return paramAmount * srcCurrency.getRate() - trgtCurrency.getRate();
    }
}
