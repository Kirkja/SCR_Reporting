package com.learnerati.datameme;

/**
 * @author      BenShaifer Jones <ben.jones@learnerati.com>
 * @version     1.0                 
 * @since       2013-06-31
 */
public class DataMeme {

    /**
     * The data being stored
     */
    private Object _value;
    private Integer _count;
    /**
     *  The descriptive label for the data
     */
    private String _label;

    
    /**
     * Creates an empty DataMeme
     */
    public DataMeme() {
        _value = null; //new Object();
        _label = new String();
        _count = 0;
    }

    
    /**
     * Creates a DataMeme with the Object as its data
     * <p>
     * 
     * @param obj Object to store as data 
     */
    public DataMeme(Object obj) {
        _value = obj;
        _label = new String();
        if (obj != null) { _count = 1; }
    }

    
    /**
     * Creates a DataMeme with a label and the Object as its data
     * <p>
     * @param str  String to use as the descriptive label
     * @param obj  Object to store as data 
     */
    public DataMeme(String str, Object obj) {
        _value = obj;
        _label = str;
        if (obj != null) { _count = 1; }
    }

    
    /**
     * Sets the label for the DataMeme
     * 
     * @param str String to use as the descriptive label
     */
    public void setLabel(String str) {
        _label = str;
    }

    
    /**
     * Loads a value into the DataMeme
     * 
     * @param obj Object to store as data 
     */
    public void setValue(Object obj) {
        _value = obj;
    }

    
    /**
     * Returns the label for the value
     * 
     * @return The descriptive label for this DataMeme
     */
    public String getLabel() {
        return _label;
    }

    /**
     * Returns the value as an Object
     * 
     * @return The Object stored as data for this DataMeme
     */
    public Object getValue() {
        return _value;
    }

    /**
     * Returns the value as an Object
     * 
     * @return The Object stored as data for this DataMeme
     */
    public Integer getCount() {
        return _count;
    }
    
    
    /**
     * Determines the class type of the value
     * 
     * @return The class Type for this DataMeme
     */
    public String getType() {
        return _value.getClass().toString();
    }

    
    /**
     * Override to represent the DataMeme as a String
     * 
     * @return The DataMeme as a String representation
     */
    @Override
    public String toString() {
        return String.format("{%s:%s}",
                _label,
                _value == null ? "null" : _value.toString());
    }

    
    /**
     * Converts the value to an Integer
     * 
     * @return The value as an Integer
     */
    public Integer asInteger() {

        if (_value instanceof Integer) {
            return (Integer) _value;
        } else if (_value instanceof Double) {
            return (int) (Math.round((Double) _value));
        } else if (_value instanceof Float) {
            return (int) (Math.round((Float) _value));
        }

        return null;
    }

    
    /**
     * Converts the value to a Double
     * 
     * @return The value as a Double
     */
    public Double asDouble() {

        if (_value instanceof Integer) {
            return new Double(_value.toString());
        } else if (_value instanceof Double) {
            return (Double) _value;
        } else if (_value instanceof Float) {
            return ((Float) _value).doubleValue();
        }

        return null;
    }

    
    /**
     * Converts the value to a Float
     * 
     * @return The value as a Float
     */
    public Float asFloat() {

        if (_value instanceof Integer) {
            return (Float) _value;
        } else if (_value instanceof Double) {
            return ((Double) _value).floatValue();
        } else if (_value instanceof Float) {
            return (Float) _value;
        }

        return null;
    }

    /**
     * Converts the value to an unformatted String
     * 
     * @return The value as an unformatted String
     */
    public String asText() {
        if (_value != null) {
            return _value.toString();
        }

        return null;
    }
    
    
    public boolean isNull() {
        if (_value != null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Converts the value to a formatted String
     * 
     * @param formatter     The string based definition to use in formatting
     * @return              The value as a a formatted String Integer
     */
    public String asText(String formatter) {
        if (_value != null) {
            return String.format(formatter, _value);
        }

        return null;
    }

    
    /**
     * Sums the provided numeric Object with this DataMeme when both values are numeric
     * 
     * @param obj A Numeric object to sum with this DataMeme
     */
    public void sum(Object obj) {
        if (_value == null) { _value = 0d; }
        
        if (_value instanceof Number && obj instanceof Number) {
            Double v = _value instanceof Double
                    ? (Double) _value : _value instanceof Float
                    ? ((Float) _value).doubleValue() : _value instanceof Integer
                    ? ((Integer) _value).doubleValue() : 0d;

            Double o = obj instanceof Double
                    ? (Double) obj : obj instanceof Float
                    ? ((Float) obj).doubleValue() : obj instanceof Integer
                    ? ((Integer) obj).doubleValue() : 0d;


            Double tmp = v + o;

            _value = _value instanceof Double
                    ? tmp : _value instanceof Float
                    ? tmp.floatValue() : _value instanceof Integer
                    ? (int) (Math.round(tmp)) : null;
            
            if (_value != null) { _count+=1; }
        }
    }
    
    
    
    /**
     * Sums the provided DataMeme with this DataMeme when both values are numeric
     * 
     * @param obj a DataMeme to sum with this item
     */
    public void sum(DataMeme obj) {
         if (obj.isNumeric()) { 
             this.sum(obj.getValue());              
         }
         if (obj != null) { _count +=1; }
    }
    
    
    
    /**
     * Determines whether or not the value stored is a Numeric value
     * 
     * @return Whether or not this DataMeme is a Numeric type
     */
    public boolean isNumeric(){
       
        if (_value instanceof Number) return true;
        
        return false;
    }
}
