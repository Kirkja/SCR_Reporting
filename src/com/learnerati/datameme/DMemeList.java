package com.learnerati.datameme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author      BenShaifer Jones <ben.jones@learnerati.com>
 * @version     1.0                 
 * @since       2013-06-31
 */
public final class DMemeList implements Iterable<DataMeme> {

    private String _label = "";
    private Double _minValue = Double.MAX_VALUE;
    private Double _maxValue = Double.MIN_VALUE;
    private Double _sum;
    private Double _abssum;
    
    private Integer _counts = 0;
    
    
    private List<DataMeme> _items = new ArrayList<>();

    
          
    
    public DMemeList() {
    }

    /**
     *
     * @param data the List of DataMeme objects to use
     */
    public DMemeList(List<DataMeme> data) {
        this._items = data;
    }

    /**
     *
     * @param str a String to use as the descriptive label
     * @param data The List of DataMeme objects to use
     */
    public DMemeList(String str, List<DataMeme> data) {
        this._label = str;
        this._items = data;
        
        Summation();
        setOutliers();
    }

    /**
     *
     * @return The descriptive label for this List
     */
    public String getLabel() {
        return this._label;
    }

    /**
     *
     * @param str a String to use as the descriptive label
     */
    public void setLabel(String str) {
        this._label = str;
    }

    /**
     *
     * @param dm the DataMeme object to be added to the List
     */
    public void addItem(DataMeme dm) {
        _items.add(dm);

        Summation();
        setOutliers();
    }

    /**
     *
     * @param position the zero based index of the item
     * @param dm the DataMeme object to be added to the List
     */
    public void addItem(int position, DataMeme dm) {
        if (_items.size() < position + 1) {
            while (_items.size() < position + 1) {
                _items.add(new DataMeme());
            }
        }

        _items.set(position, dm);
        
        Summation();
        setOutliers();
    }
    
    
    /**
     *
     * @param position the zero based index of the item
     * @param dm the DataMeme object to be added to the List
     */
    public void sumItem(int position, DataMeme dm) {
        if (_items.size() < position + 1) {
            while (_items.size() < position + 1) {
                _items.add(new DataMeme());
            }
        }

        DataMeme tmp = _items.get(position);
        tmp.sum(dm);
        _items.set(position, tmp);
        
        Summation();
        setOutliers();
    }    

    /**
     *
     * @param data the List of DataMeme objects to use
     */
    public void replaceItems(List<DataMeme> data) {
        this._items.clear();
        this._items.addAll(data);

        Summation();
        setOutliers();
    }

    /**
     * 
     * @param position the zero based index of the item
     */
    public void removeItem(int position) {
        
        if (position < _items.size()) {
            this._items.remove(position);
        }
        
        Summation();
        setOutliers();
    }    
    
    
    /**
     * 
     * @param data the DataMeme object to remove from the List
     */
    public void removeItem(DataMeme data) {
        this._items.remove(data);
        
        Summation();
        setOutliers();
    }  
    
    
    /**
     *
     * @param position the zero based index of the item
     * @return The DataMeme stored at that index
     */
    public DataMeme get(int position) {
        if (position < _items.size()) {
            return _items.get(position);
        }

        return null;
    }

    /**
     *
     * @return A List of all the DataMemes
     */
    public List<DataMeme> get() {
        return _items;
    }

    //-------------------------------------------------------------------------
    // Utilities
    //
    /**
     *
     * @param n A value to check against the min and max for the List
     */
    private void Outlier(Double n) {
        //if (n.IsNumeric())
        {
            if (_minValue == null) {
                _minValue = Double.MAX_VALUE;
            }

            if (_maxValue == null) {
                _maxValue = Double.MIN_VALUE;
            }

            _minValue = (n < _minValue) ? n : _minValue;
            _maxValue = (n > _maxValue) ? n : _maxValue;
        }
    }

    /**
     *
     */
    public void Reverse() {
        Collections.reverse(_items);
    }

    /**
     *
     */
    private void setOutliers() {
        _minValue = Double.MAX_VALUE;
        _maxValue = Double.MIN_VALUE;

        for (DataMeme dm : _items) {
            if (dm.isNumeric()) {
                Outlier(dm.asDouble());
            }
        }
    }

    /**
     * Returns 
     * @return The minimum value in the List as a Double
     */
    public Double Min() {
        return this._minValue;
    }

    /**
     * Returns 
     * @return The maximum value in the List as a Double
     */
    public Double Max() {
        return this._maxValue;
    }

    /**
     * 
     * @return Return the total summed value
     */
    public Double Sum()
    {
        return _sum;
    }
 
    /**
     * 
     * @return The total counts for all elements of the list
     */
    public Integer Counts()
    {
        return _counts;
    }    
    
    /**
     * 
     * @return 
     */
    public Double ABSsum()
    {
        return _abssum;
    }    
    
    
    /**
     * 
     */
    private void Summation()
    {
        _sum    = null;
        _abssum = null;
        _counts = 0;
        
        for (DataMeme dm : _items)
        {
            //_sum    = _sum == null      ? dm.asDouble() : (_sum + dm.asDouble());
            //_abssum = _abssum == null   ? Math.abs(dm.asDouble()) : (_abssum + Math.abs(dm.asDouble()));
            
            if (dm.asDouble() != null)
            {
                _sum    = _sum == null      ? dm.asDouble() : (_sum + dm.asDouble());
                _abssum = _abssum == null   ? Math.abs(dm.asDouble()) : (_abssum + Math.abs(dm.asDouble()));
            }
            
            _counts += dm.getCount();
        }
    }
    
    
    
    /**
     * Returns 
     * @return The number of items in the List as an integer
     */
    public int size() {
        return _items.size();
    }

    /**
     *
     * @return A String composite representing the List
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Label:%s\nItems [", getLabel()));

        for (DataMeme dm : _items) {
            sb.append(String.format("%s, ", dm.asText()));
        }

        sb.append("]");
        sb.append(String.format("\nmin:%f \nmax:%f", _minValue, _maxValue));

        return sb.toString();
    }

    @Override
    public Iterator<DataMeme> iterator() {
        Iterator<DataMeme> iprof = _items.iterator();
        return iprof;
    }
    
    
}
