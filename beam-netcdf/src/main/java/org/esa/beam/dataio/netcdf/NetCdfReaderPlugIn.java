/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.beam.dataio.netcdf;

import org.esa.beam.dataio.netcdf.metadata.ProfileSpi;
import org.esa.beam.dataio.netcdf.metadata.ProfileSpiRegistry;
import org.esa.beam.dataio.netcdf.util.Constants;
import org.esa.beam.framework.dataio.DecodeQualification;
import org.esa.beam.framework.dataio.ProductReader;
import org.esa.beam.framework.dataio.ProductReaderPlugIn;
import org.esa.beam.framework.dataio.ProfileReaderPlugIn;
import org.esa.beam.util.io.BeamFileFilter;
import ucar.nc2.NetcdfFile;

import java.io.IOException;
import java.util.Locale;


/**
 * The plug-in class for the {@link NetCdfReader}.
 *
 * @author Norman Fomferra
 */
public class NetCdfReaderPlugIn implements ProductReaderPlugIn, ProfileReaderPlugIn {

    private String profileClassName;

    public NetCdfReaderPlugIn() {
    }

    public NetCdfReaderPlugIn(String profileClassName) {
        this.profileClassName = profileClassName;
    }

    /**
     * Returns true only if all of the following conditions are true:
     * <ul>
     * <li>the given input object's string representation has the ".nc" extension,</li>
     * <li>the given input object's string representation points to a valid netCDF file,</li>
     * <li>the netCDF file has variables which can be interpreted as bands.</li>
     * </ul>
     */
    public DecodeQualification getDecodeQualification(final Object input) {

        NetcdfFile netcdfFile = null;
        try {
            netcdfFile = NetcdfFile.open(input.toString());
            if (netcdfFile == null) {
                return DecodeQualification.UNABLE;
            }
            ProfileSpiRegistry profileSpiRegistry = ProfileSpiRegistry.getInstance();
            if (profileClassName != null) {
                ProfileSpi profileFactory = profileSpiRegistry.getProfileFactory(profileClassName);
                if (profileFactory != null) {
                    return profileFactory.getDecodeQualification(netcdfFile);
                }
            } else {
                return profileSpiRegistry.getDecodeQualification(netcdfFile);
            }
        } catch (Exception ignore) {
        } finally {
            try {
                if (netcdfFile != null) {
                    netcdfFile.close();
                }
            } catch (IOException e) {
                // OK, ignored
            }
        }
        return DecodeQualification.UNABLE;
    }

    /**
     * Returns an array containing the classes that represent valid input types for this reader.
     * <p/>
     * <p> Intances of the classes returned in this array are valid objects for the <code>setInput</code> method of the
     * <code>ProductReader</code> interface (the method will not throw an <code>InvalidArgumentException</code> in this
     * case).
     *
     * @return an array containing valid input types, never <code>null</code>
     */
    public Class[] getInputTypes() {
        return Constants.READER_INPUT_TYPES;
    }

    /**
     * Creates an instance of the actual product reader class. This method should never return <code>null</code>.
     *
     * @return a new reader instance, never <code>null</code>
     */
    public ProductReader createReaderInstance() {
        return new NetCdfReader(this, profileClassName);
    }

    public BeamFileFilter getProductFileFilter() {
        if (profileClassName != null) {
            ProfileSpiRegistry profileSpiRegistry = ProfileSpiRegistry.getInstance();
            ProfileSpi profileFactory = profileSpiRegistry.getProfileFactory(profileClassName);
            if (profileFactory != null) {
                BeamFileFilter fileFilter = profileFactory.getProductFileFilter();
                if (fileFilter != null) {
                    return fileFilter;
                }
            }
        }
        return new BeamFileFilter(getFormatNames()[0], getDefaultFileExtensions(), getDescription(null));
    }

    /**
     * Gets the names of the product formats handled by this product I/O plug-in.
     *
     * @return the names of the product formats handled by this product I/O plug-in, never <code>null</code>
     */
    public String[] getFormatNames() {
        return new String[]{Constants.FORMAT_NAME};
    }

    /**
     * Gets the default file extensions associated with each of the format names returned by the <code>{@link
     * #getFormatNames}</code> method. <p>The string array returned shall always have the same lenhth as the array
     * returned by the <code>{@link #getFormatNames}</code> method. <p>The extensions returned in the string array shall
     * always include a leading colon ('.') character, e.g. <code>".hdf"</code>
     *
     * @return the default file extensions for this product I/O plug-in, never <code>null</code>
     */
    public String[] getDefaultFileExtensions() {
        return Constants.FILE_EXTENSIONS;
    }

    /**
     * Gets a short description of this plug-in. If the given locale is set to <code>null</code> the default locale is
     * used.
     * <p/>
     * <p> In a GUI, the description returned could be used as tool-tip text.
     *
     * @param locale the local for the given decription string, if <code>null</code> the default locale is used
     *
     * @return a textual description of this product reader/writer
     */
    public String getDescription(final Locale locale) {
        if (profileClassName != null) {
            int lastDotIndex = profileClassName.lastIndexOf(".");
            String shortName = profileClassName.substring(lastDotIndex+1);
            return Constants.FORMAT_DESCRIPTION + " (" + shortName + ")";
        } else {
            return Constants.FORMAT_DESCRIPTION;
        }
    }

    @Override
    public ProductReaderPlugIn createProfileReaderPlugin(String profileClassName) {
        return new NetCdfReaderPlugIn(profileClassName);
    }
}