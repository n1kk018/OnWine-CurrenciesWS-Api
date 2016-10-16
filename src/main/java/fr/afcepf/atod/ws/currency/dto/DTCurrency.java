package fr.afcepf.atod.ws.currency.dto;

import java.io.Serializable;
/**
 * Data Transfer Object of the currency Entity.
 * @author nikko
 *
 */
public class DTCurrency implements Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -3255475366427129373L;
    /**
     * unique identifier of the DTcurrency.
     */
    private Integer id;
    /**
     * Currency name.
     */
    private String name;
    /**
     * Currency code.
     */
    private String code;
    /**
     * Exchange rate based on dollar.
     */
    private Double rate;
    /**
     * Default constructor of DTCurrency.
     */
    public DTCurrency() {
        super();
    }
    /**
     * Constructor of DTCurrency with parameters.
     * @param paramId id parameter
     * @param paramName name parameter
     * @param paramCode code parameter
     * @param paramRate rate parameter
     */
    public DTCurrency(Integer paramId, String paramName, String paramCode,
           Double paramRate) {
        super();
        id = paramId;
        name = paramName;
        code = paramCode;
        rate = paramRate;
    }
    /**
     * public accessor for id.
     * @return Integer id
     */
    public Integer getId() {
        return id;
    }
    /**
     * public mutator for id.
     * @param paramId id
     */
    public void setId(Integer paramId) {
        id = paramId;
    }
    /**
     * public accessor for name.
     * @return String name
     */
    public String getName() {
        return name;
    }
    /**
     * public mutator for name.
     * @param paramName name
     */
    public void setName(String paramName) {
        name = paramName;
    }
    /**
     * public accessor for code.
     * @return String code
     */
    public String getCode() {
        return code;
    }
    /**
     * public mutator for code.
     * @param paramCode code
     */
    public void setCode(String paramCode) {
        code = paramCode;
    }
    /**
     * public accessor for rate.
     * @return Double rate
     */
    public Double getRate() {
        return rate;
    }
    /**
     * public mutator for rate.
     * @param paramRate rate
     */
    public void setRate(Double paramRate) {
        rate = paramRate;
    }
    @Override
    public String toString() {
        return "DTCurrency [id=" + id + ", name=" + name + ", code=" + code
                + ", rate=" + rate + "]";
    }
}
