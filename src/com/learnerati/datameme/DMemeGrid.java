package com.learnerati.datameme;

import java.util.ArrayList;
import java.util.List;


/**
 * @author      Ben S. Jones <ben.jones@learnerati.com>
 * @version     1.0                 
 * @since       2013-06-31
 */
public class DMemeGrid {

    private String _label;
    private Double _minValue;
    private Double _maxValue;
    private Double _totalValue; 
    private Integer _totalCount;
    
    private List<String> _rowLabels;
    private List<String> _colLabels;
    private String _rowDescriptor;
    private String _colDescriptor;
    private DMemeList _rowSummary;
    private DMemeList _colSummary;
    private List<DMemeList> _grid;
    private int _cols;

    
    /**
     *
     */
    public DMemeGrid() {
        _minValue = Double.MAX_VALUE;
        _maxValue = -Double.MAX_VALUE;

        _rowLabels = new ArrayList<>();
        _colLabels = new ArrayList<>();
        _grid = new ArrayList<>();

        _rowSummary = new DMemeList();
        _colSummary = new DMemeList();
    }

    /**
     *
     * @return The descriptive label for the grid
     */
    public String getLabel() {
        return _label;
    }

    /**
     *
     * @param str a String to use as the descriptive label for the grid
     */
    public void setLabel(String str) {
        this._label = str;
    }

    /**
     *
     * @return The number of rows in the grid
     */
    public int Rows() {
        return _grid.size();
    }

    /**
     *
     * @return The widest number of columns in the grid
     */
    public int Cols() {
        return _cols;
    }

    /**
     *
     * @return The maximum numeric value in the grid
     */
    public Double Min() {
        return _minValue;
    }

    /**
     *
     * @return The minimum numeric value in the grid
     */
    public Double Max() {
        return _maxValue;
    }
    
    
    /**
     *
     * @return The count value for all elements
     */
    public Integer Count() {
        return _totalCount;
    }    

    
    /**
     *
     * @return The total value for all elements
     */
    public Double Total() {
        return _totalValue;
    } 
    
    
    public boolean hasElement(int row, int col) {
        if (row < Rows()) {
            if (col < _grid.get(row).size()) {
                return true;
            }
        }

        return false;        
    }    
    
    
    
    //------------------------------------------------------------------------
    // Row related methods 
    //
    //
    /**
     *
     * @return The description label for all the rows
     */
    public String getRowDescriptor() {
        return _rowDescriptor;
    }

    /**
     *
     * @param str the description label for all the rows
     */
    public void setRowDescriptor(String str) {
        _rowDescriptor = str;
    }

    /**
     *
     * @param position a zero based index position
     * @return The description label for specific row
     */
    public String getRowLabel(int position) {
        if (position < _rowLabels.size()) {
            return _rowLabels.get(position);
        }

        return null;
    }

    
    /**
     * 
     * @param position 
     */
    public void removeRowLabel(int position) {
        if (position < _rowLabels.size()) {
            //return _rowLabels.get(position);
            _rowLabels.remove(position);
        }

        //return null;
    }

        
    
    /**
     *
     * @return A List of all row labels
     */
    public List<String> getRowLabels() {
        return _rowLabels;
    }

    /**
     *
     * @param str a descriptive string label for the row
     */
    public void addRowLabel(String str) {
        _rowLabels.add(str);
    }

    /**
     *
     * @param position a zero based index position
     * @param str a descriptive string label for the row
     */
    public void addRowLabel(int position, String str) {
        if (position >= _rowLabels.size()) {
            while (_rowLabels.size() < position + 1) {
                _rowLabels.add(null);
            }
        }
        _rowLabels.set(position, str);
    }

    
    /**
     *
     * @param strs a List of descriptive label strings for the rows
     */
    public void replaceRowLabels(List<String> strs) {
        _rowLabels = strs;
    }

    /**
     *
     * @param position a zero based index value
     * @return A copy of the row
     */
    public DMemeList CopyRow(int position) {
        DMemeList tmp = new DMemeList();

        if (position < Rows()) {
            tmp.replaceItems(_grid.get(position).get());
            tmp.setLabel(_grid.get(position).getLabel());
        }

        return tmp;
    }

    
    /**
     *
     * @param position a zero based index value
     * @return requested row by index
     */
    public DMemeList getRow(int position) {
        DMemeList tmp = new DMemeList();

        if (position < Rows()) {
            tmp = _grid.get(position);
        }

        return tmp;
    }
    
    
    
    /**
     *
     * @param data a DMemeList to use as a new row
     */
    public void addRow(DMemeList data) {
        _grid.add(data);

        setOutliers();

        _cols = data.size() > _cols ? data.size() : _cols;
    }

    
    
    /**
     * 
     * @param position a zero based index value
     * @param data a DMemeList to use as a row replacement
     */
    public void addRow(int position, DMemeList data) {
        
        if (position < _grid.size())
        {
            _grid.set(position, data);
        }
        else
        {
            _grid.add(data);
        }
        
        setOutliers();

       _cols = data.size() > _cols ? data.size() : _cols;
    }
    
    

    /**
     * 
     * @param position a zero based index value
     * @return A copy of the row removed from the grid
     */
    public DMemeList removeRow(int position)
    {
        DMemeList tmp = null;
        
        if (position < _grid.size())
        {
            tmp = _grid.get(position);
            _grid.remove(position);
        }
        setOutliers();
        return tmp;
    }
    
    
    //------------------------------------------------------------------------
    // Column related methods 
    //
    
    
    public Double getColTotal(int position) {
        Double total = 0d;
        
        if (position < Cols()) {
            DMemeList tmp = copyColumn(position);
            if (tmp.size() > 0) {
                for (DataMeme dm : tmp) {
                    Double i = dm.asDouble();
                    if (i != null) { total += i; }
                }
            }
        }
                
        return total;
    }
    
    
    /**
     *
     * @return The descriptive label for all the columns
     */
    public String getColDescriptor() {
        return _colDescriptor;
    }

    /**
     *
     * @param str the descriptive label for all the columns
     */
    public void setColDescriptor(String str) {
        _colDescriptor = str;
    }

    /**
     *
     * @return A List of all the column labels
     */
    public List<String> getColLabels() {
        return _colLabels;
    }

    /**
     *
     * @param idx a zero based index
     * @return The descriptive label for this column
     */
    public String getColLabel(int idx) {
        if (idx < _colLabels.size()) {
            return _colLabels.get(idx);
        }

        return null;
    }

    /**
     *
     * @param idx
     */
    public void removeColLabel(int idx) {
        if (idx < _colLabels.size()) {
            _colLabels.remove(idx);
        }

        //return null;
    }
    
    
    
    /**
     *
     * @param idx a zero based index
     * @param str the descriptive label to use for this column
     */
    public void addColLabel(int idx, String str) {
        if (idx >= _colLabels.size()) {
            while (_colLabels.size() < idx + 1) {
                _colLabels.add(null);
            }
        }
        _colLabels.set(idx, str);
    }

    /**
     *
     * @param str a descriptive label for the column
     */
    public void addColLabel(String str) {
        _colLabels.add(str);
    }

    /**
     *
     * @param strs a List of descriptive labels for all the columns
     */
    public void replaceColLabels(List<String> strs) {
        _colLabels = strs;
    }

    /**
     *
     * @param position a zero based index
     * @return A copy of the column
     */
    public DMemeList copyColumn(int position) {
        DMemeList tmp = new DMemeList();

        for (DMemeList ds : _grid) {
            if (position < ds.size()) {
                tmp.addItem(ds.get(position));
            }
        }

        if (position < _colLabels.size()) {
            tmp.setLabel(_colLabels.get(position));
        }

        return tmp;
    }

    /**
     *
     * @param data a DMemeList to use as column data
     */
    public void addColumn(DMemeList data) {

        int idx = 0;

        for (DMemeList dml : _grid) {
            if (idx < data.size()) {
                dml.addItem(_cols, data.get(idx++));
            }
        }
        setOutliers();
        _cols += 1;
    }

    
    
    /**
     * 
     * @param position a zero based index
     * @param data a DMemeList to use as column data
     */
    public void addColumn(int position, DMemeList data) {
        
        int idx = 0;
        
        for (DMemeList dml : _grid) {
            if (idx < data.size()) {
                dml.addItem(position, data.get(idx++));
            }
        }
        setOutliers();        
        _cols = position < _cols ? _cols : position;
    }

    
    
   /**
    * 
    * @param position a zero based index
     * @return 
    */ 
   public DMemeList removeColumn(int position)
   {
       DMemeList tmp = new DMemeList();
       
        for (DMemeList dml : _grid) {
            if (position < _cols) {   
                tmp.addItem(dml.get(position));
                dml.removeItem(position);
            }
        }
            
        setOutliers();
        _cols -= 1;    
        
        return tmp;
   }
    
    

       
   
    //-------------------------------------------------------------------------
    //
    //
    /**
     *
     * @param row a zero based index for the rows
     * @param col a zero based index for the columns
     * @param dm a DataMeme object to use as data
     */
    public void addItem(int row, int col, DataMeme dm) {
        if (_grid.size() < row + 1) {
            while (_grid.size() < row + 1) {
                _grid.add(new DMemeList());
            }
        }

        _grid.get(row).addItem(col, dm);
        _cols = col + 1 > _cols ? col + 1 : _cols;

        setOutliers();
    }

    //-------------------------------------------------------------------------
    //
    //
    /**
     *
     * @param row a zero based index for the rows
     * @param col a zero based index for the columns
     * @param dm a DataMeme object to use as data
     */
    public void sumItem(int row, int col, DataMeme dm) {
        if (_grid.size() < row + 1) {
            while (_grid.size() < row + 1) {
                _grid.add(new DMemeList());
            }
        }

        _grid.get(row).sumItem(col, dm);
        _cols = col + 1 > _cols ? col + 1 : _cols;

        setOutliers();
    }
    
    
    
    /**
     *
     * @param row a zero based index for the rows
     * @param col a zero based index for the columns
     * @return The DataMeme object stored at this location
     */
    public DataMeme getItem(int row, int col) {
        
        if (row > Rows()) {
            return null;
        }
        
        if (col > _grid.get(row).size()) {
            return null;
        }
                
        return _grid.get(row).get(col);        
    }

    //-----------------------------------------------------------------------
    /**
     *
     * @param da a DataMeme object used for grid metrics
     */
    public void addRowSummaryItem(DataMeme da) {
        _rowSummary.addItem(da);
    }

    /**
     *
     * @param position a zero based index
     * @param da  a DataMeme object used for grid metrics
     */
    public void addRowSummaryItem(int position, DataMeme da) {
        _rowSummary.addItem(position, da);
    }

    /**
     *
     * @return a DMemeList of row summary metrics
     */
    public DMemeList getRowSummarys() {
        return _rowSummary;
    }

    /**
     *
     * @return a DMemeList of column summary metrics
     */
    public DMemeList getColSummarys() {
        return _colSummary;
    }

    /**
     *
     * @return The descriptive label for the row summary metrics
     */
    public String getRowSummaryLabel() {
        return _rowSummary.getLabel();
    }

    /**
     *
     * @param str a descriptive label for the column summary metrics
     */
    public void setRowSummaryLabel(String str) {
        _rowSummary.setLabel(str);
    }

    /**
     *
     * @param position a zero based index
     * @return The DataMeme for a particular row metric
     */
    public DataMeme getRowSummaryItem(int position) {
        return _rowSummary.get(position);
    }

    /**
     *
     * @return The descriptive column metric label
     */
    public String getColSummaryLabel() {
        return _colSummary.getLabel();
    }

    /**
     *
     * @param str a descriptive colum metric label
     */
    public void setColSummaryLabel(String str) {
        _colSummary.setLabel(str);
    }

    /**
     *
     * @param da the data being stored as a column metric
     */
    public void addColSummaryItem(DataMeme da) {
        _colSummary.addItem(da);
    }

    /**
     *
     * @param position a zero based index
     * @param da data to be stored as a column metric
     */
    public void addColSummaryItem(int position, DataMeme da) {
        _colSummary.addItem(position, da);
    }

    /**
     *
     * @param position a zero based index
     * @return The data stored for this column metric
     */
    public DataMeme getColSummaryItem(int position) {
        return _colSummary.get(position);
    }

    

    //------------------------------------------------------------------------
    // Utility methods
    /**
     *
     */
    private void setOutliers() {

        _minValue = Double.MAX_VALUE;
        _maxValue = Double.MIN_VALUE;
        _totalCount = 0;
        _totalValue = 0d;
        
        for (DMemeList dml : _grid) {
            
            if (dml.Min() != null && dml.Max() != null) {
                if (_minValue == null) {
                    _minValue = dml.Min();
                }

                if (_maxValue == null) {
                    _maxValue = dml.Max();
                }

                _minValue = (dml.Min() < _minValue) ? dml.Min() : _minValue;
                _maxValue = (dml.Max() > _maxValue) ? dml.Max() : _maxValue;
            }
            
            _totalCount += dml.Counts();
            _totalValue += dml.Sum() != null ? dml.Sum() : 0d;
        }
    }

    /**
     * Just a simple dump 
     */
    public void DumpGrid() {

        String formatter = "%4s";

        System.out.printf("\nDataGrid has the following attributes:"
                + "\nTitle = [%s]\nMin = %f\nMax = %f\nRows = %d\nCols = %d"
                + "\nCount = %d " 
                + "\nTotal = %f "
                + "\nColumn Labels = %s\nRow Labels = %s"
                + "\nRowDesc = %s\nColDesc = %s",
                getLabel(), Min(), Max(), Rows(), Cols(), 
                Count(),
                Total(),
                _colLabels.toString(), _rowLabels.toString(),
                getRowDescriptor(), getColDescriptor());

        if (_rowSummary.size() > 0) {
            System.out.printf("\nRow Summary = %s", _rowSummary);
        }
        if (_colSummary.size() > 0) {
            System.out.printf("\nCol Summary = %s", _colSummary);
        }

        System.out.println("\nValues = ");
        for (DMemeList dml : _grid) {
            
            for (DataMeme dm : dml) {
                System.out.printf("%10s (%5d)", dm.asText(), dm.getCount());
            }
            System.out.println();
        }

    }
}
