package org.esa.beam.visat.toolviews.pin;

import com.bc.ceres.core.Assert;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.GcpGeoCoding;
import org.esa.beam.framework.datamodel.GeoCoding;
import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.Pin;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.beam.framework.datamodel.PlacemarkDescriptor;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.TiePointGrid;

public class GcpTableModel extends AbstractPlacemarkTableModel {

    public GcpTableModel(PlacemarkDescriptor placemarkDescriptor, Product product, Band[] selectedBands,
                         TiePointGrid[] selectedGrids) {
        super(placemarkDescriptor, product, selectedBands, selectedGrids);
    }

    @Override
    public String[] getStandardColumnNames() {
        return new String[]{"X", "Y", "Lon", "Lat", "DLon", "DLat", "Label"};
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex < getStandardColumnNames().length && columnIndex != 4 && columnIndex != 5;
    }

    @Override
    protected Object getStandardColumnValueAt(int rowIndex, int columnIndex) {
        Assert.notNull(getProduct());
        final Pin pin = getPlacemarkDescriptor().getPlacemarkGroup(getProduct()).get(rowIndex);

        float x = Float.NaN;
        float y = Float.NaN;

        final PixelPos pixelPos = pin.getPixelPos();
        if (pixelPos != null) {
            x = pixelPos.x;
            y = pixelPos.y;
        }

        float lon = Float.NaN;
        float lat = Float.NaN;

        final GeoPos geoPos = pin.getGeoPos();
        if (geoPos != null) {
            lon = geoPos.lon;
            lat = geoPos.lat;
        }

        float dLon = Float.NaN;
        float dLat = Float.NaN;

        final GeoCoding geoCoding = getProduct().getGeoCoding();

        if (geoCoding instanceof GcpGeoCoding && pixelPos != null) {
            final GeoPos expectedGeoPos = geoCoding.getGeoPos(pixelPos, new GeoPos());
            if (expectedGeoPos != null) {
                dLon = lon - expectedGeoPos.lon;
                dLat = lat - expectedGeoPos.lat;
            }
        }

        switch (columnIndex) {
        case 0:
            return x;
        case 1:
            return y;
        case 2:
            return lon;
        case 3:
            return lat;
        case 4:
            return dLon;
        case 5:
            return dLat;
        case 6:
            return pin.getLabel();
        default:
            return "";
        }
    }
}
