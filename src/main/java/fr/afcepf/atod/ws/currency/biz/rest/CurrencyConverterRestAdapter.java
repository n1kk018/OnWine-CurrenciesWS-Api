package fr.afcepf.atod.ws.currency.biz.rest;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

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
@Path("/converter")
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyConverterRestAdapter
    implements ICurrencyConverter, Serializable {
    static {
        REFCURRENCYMAP.put("EUR", "flaticon-euro-currency-symbol");
        REFCURRENCYMAP.put("GBP", "flaticon-pound-symbol-variant");
        REFCURRENCYMAP.put("USD", "flaticon-dollar-currency-symbol-2");
        REFCURRENCYMAP.put("JPY", "flaticon-yen-currency-symbol");
        REFCURRENCYMAP.put("BGN", "flaticon-bulgaria-lev");
        REFCURRENCYMAP.put("DKK", "flaticon-denmark-krone-currency-symbol");
        REFCURRENCYMAP.put("EEK", "flaticon-estonia-kroon-currency-symbol");
        REFCURRENCYMAP.put("HUF", "flaticon-hungary-forint-currency-symbol");
        REFCURRENCYMAP.put("LVL", "flaticon-latvia-lat");
        REFCURRENCYMAP.put("LTL", "flaticon-lithuania-litas-currency-symbol");
        REFCURRENCYMAP.put("PLN", "flaticon-poland-zloty-currency-symbol");
        REFCURRENCYMAP.put("CZK", "flaticon-czech-republic-koruna-currency-symbol");
        REFCURRENCYMAP.put("SKK", "flaticon-denmark-krone-currency-symbol");
        REFCURRENCYMAP.put("SEK", "flaticon-sweden-krona-currency-symbol");
    }
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
     * Source currency.
     */
    private Currency srcCurrency;
    /**
     * Target currency.
     */
    private Currency trgtCurrency;
    
    private final static Logger log = Logger.getLogger( RESTCorsDemoRequestFilter.class.getName() );
    /**
     * test.
     */
    @GET
    @Path("/listAll")
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/convert")
    @Override
    public Double convert(@QueryParam("amount") Double paramAmount,
            @QueryParam("src") String paramSrcCurrency,
            @QueryParam("trgt") String paramTrgtCurrency)
            throws CurrenciesWSException {
        log.info("====================================================");
        log.info(paramSrcCurrency);
        srcCurrency = dao.findByCode(paramSrcCurrency);
        trgtCurrency = dao.findByCode(paramTrgtCurrency);
        Double amountInDollar = paramAmount / srcCurrency.getRate();
        return  amountInDollar * trgtCurrency.getRate();
    }
    /**
     * Version formatté que pour le REST.
     * @param paramAmount
     * @param paramSrcCurrency
     * @param paramTrgtCurrency
     * @return 
     * @throws CurrenciesWSException
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/convertAndFormat")
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public String convertAndFormat(@QueryParam("amount") Double paramAmount,
            @QueryParam("src") String paramSrcCurrency,
            @QueryParam("trgt") String paramTrgtCurrency)
            throws CurrenciesWSException {
        log.info("====================================================");
        log.info(paramSrcCurrency);
        srcCurrency = dao.findByCode(paramSrcCurrency);
        trgtCurrency = dao.findByCode(paramTrgtCurrency);
        DecimalFormat df = new DecimalFormat("########.00");  
        Double result = (paramAmount / srcCurrency.getRate()) * trgtCurrency.getRate();
        return df.format(result).replace(".", ",")
                + " <span class='" + REFCURRENCYMAP.get(paramTrgtCurrency)+"'></span>";
    }
    /**
     * @return the srcCurrency
     */
    public Currency getSrcCurrency() {
        return srcCurrency;
    }
    /**
     * @param paramSrcCurrency the srcCurrency to set
     */
    public void setSrcCurrency(Currency paramSrcCurrency) {
        srcCurrency = paramSrcCurrency;
    }
    /**
     * @return the trgtCurrency
     */
    public Currency getTrgtCurrency() {
        return trgtCurrency;
    }
    /**
     * @param paramTrgtCurrency the trgtCurrency to set
     */
    public void setTrgtCurrency(Currency paramTrgtCurrency) {
        trgtCurrency = paramTrgtCurrency;
    }
    /**
     * @param paramDao the dao to set
     */
    public void setDao(ICurrencyDao paramDao) {
        dao = paramDao;
    }
}
