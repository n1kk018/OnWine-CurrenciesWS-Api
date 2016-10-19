package fr.afcepf.atod.ws.currency.biz.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import fr.afcepf.atod.ws.currency.biz.api.ICurrencyConverter;
import fr.afcepf.atod.ws.currency.client.RestClient;
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
     * un logger.
     */
    private static Logger log = Logger.getLogger(CurrencyConverter.class);
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -3023413234131773165L;
    /**
     * millisecond multiplier for unix timestamps conversion.
     */
    private static final long MILLIS = 1000L;
    /**
     * Used to remove a day on a date via calendar object.
     */
    private static final int MOINSUNJOUR = -24;
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
    @Override
    public List<DTCurrency> getAllCurrencies() throws CurrenciesWSException {
        List<DTCurrency> listDTO  = new ArrayList<DTCurrency>();
        try {
            for (Currency  c : dao.findAll()) {
                listDTO.add(entityDevise2DeviseDTO(c));
            }
        } catch (CurrenciesWSException paramE) {
            createDB();
            for (Currency  c : dao.findAll()) {
                listDTO.add(entityDevise2DeviseDTO(c));
            }
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
        try {
            srcCurrency = dao.findByCode(paramSrcCurrency);
            trgtCurrency = dao.findByCode(paramTrgtCurrency);
        } catch (CurrenciesWSException e) {
            createDB();
            try {
                srcCurrency = dao.findByCode(paramSrcCurrency);
                trgtCurrency = dao.findByCode(paramTrgtCurrency);
            } catch (Exception paramE) {
                throw new CurrenciesWSException("Devise non référencée!");
            }
        }
        Calendar cal = new GregorianCalendar(
                TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        log.info("======================================");
        log.info(cal.getTime());
        log.info(srcCurrency.getUpdatedAt());
        log.info(cal.getTime().after(srcCurrency.getUpdatedAt()));
        if (cal.getTime().after(srcCurrency.getUpdatedAt())) {
            updateDB();
        }
        Double amountInDollar = paramAmount / srcCurrency.getRate();
        return  amountInDollar * trgtCurrency.getRate();
    }
    /**
     * Initial create of the rates via REST ws.
     * @throws CurrenciesWSException custom exception
     */
    private void createDB() throws CurrenciesWSException {
        try {
            RestClient updater = new RestClient();
            JSONObject currencies = new JSONObject(updater.getCurrencies());
            JSONObject mainObj = new JSONObject(updater.getLatestRates());
            JSONObject rates = mainObj.getJSONObject("rates");
            for (Iterator<String> iterator = rates.keys();
                    iterator.hasNext();) {
                Currency c = null;
                String key = (String) iterator.next();
                c = new Currency(null,
                    currencies.getString(key),
                    key,
                    rates.getDouble(key));
                dao.insert(c);
            }
        } catch (CurrenciesWSException paramE) {
            throw new CurrenciesWSException(paramE.getMessage(),
                    paramE.getWsError());
        }
    }
     /**
     * Update of the rates once a day via REST ws.
     * @throws CurrenciesWSException custom exception
     */
    private void updateDB() throws CurrenciesWSException {
        try {
            RestClient updater = new RestClient();
            JSONObject currencies = new JSONObject(updater.getCurrencies());
            JSONObject mainObj = new JSONObject(updater.getLatestRates());
            JSONObject rates = mainObj.getJSONObject("rates");
            for (Iterator<String> iterator = rates.keys();
                    iterator.hasNext();) {
                Currency c = null;
                String key = (String) iterator.next();
                try {
                    c = dao.findByCode(key);
                } catch (CurrenciesWSException e) { }
                if (c == null) {
                    c = new Currency(null,
                        currencies.getString(key),
                        key,
                        rates.getDouble(key));
                    dao.insert(c);
                } else {
                    c.setName(currencies.getString(key));
                    c.setCode(key);
                    c.setRate(rates.getDouble(key));
                    c.setUpdatedAt(new Date(mainObj.getLong("timestamp")
                            * MILLIS));
                    dao.update(c);
                }
            }
        } catch (CurrenciesWSException paramE) {
            throw new CurrenciesWSException(paramE.getMessage(),
                    paramE.getWsError());
        }
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
}
