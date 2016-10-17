package fr.afcepf.atod.ws.currency.biz.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import fr.afcepf.atod.ws.currency.biz.api.ICurrencyConverter;
import fr.afcepf.atod.ws.currency.dao.api.ICurrencyDao;
import fr.afcepf.atod.ws.currency.dto.DTCurrency;
import fr.afcepf.atod.ws.currency.entity.Currency;
import fr.afcepf.atod.ws.currency.exception.CurrenciesWSException;
/**
 * Implementation of rest adapter for the Currency converter WS.
 * @author nikko
 *
 */
@Stateless
@Path("/rest")
@Produces("application/json")
public class CurrencyConverterRestAdapter
    implements ICurrencyConverter, Serializable {
    /**
     * serialization ID.
     */
    private static final long serialVersionUID = 2617957615153799869L;
    /**
     * injected currency dao.
     */
    @EJB
    private ICurrencyDao dao;
    /**
     * test.
     */
    @GET
    @Path("/listAllCurrencies")
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
}