package fr.afcepf.atod.ws.currency.biz.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
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
        srcCurrency = dao.findByCode(paramSrcCurrency);
        if (srcCurrency != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(srcCurrency.getUpdatedAt());
            cal.add(Calendar.HOUR, MOINSUNJOUR);
            if (srcCurrency.getUpdatedAt().before(cal.getTime())) {
                updateDB();
            }
        }
        trgtCurrency = dao.findByCode(paramTrgtCurrency);
        return paramAmount * srcCurrency.getRate() - trgtCurrency.getRate();
    }
     /**
     * Update of the rates once a day via REST ws.
     */
    private void updateDB() {
        try {
            RestClient updater = new RestClient();
            JSONObject currencies = new JSONObject(updater.getCurrencies());
            JSONObject mainObj = new JSONObject(updater.getLatestRates());
            JSONObject rates = mainObj.getJSONObject("rates");
            for (Iterator<String> iterator = rates.keys();
                    iterator.hasNext();) {
                Currency c = null;
                String key = (String) iterator.next();
                c = dao.findByCode(key);
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
            paramE.printStackTrace();
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
