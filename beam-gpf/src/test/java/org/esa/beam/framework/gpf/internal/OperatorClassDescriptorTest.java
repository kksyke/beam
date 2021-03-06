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

package org.esa.beam.framework.gpf.internal;

import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.gpf.OperatorException;
import org.esa.beam.framework.gpf.annotations.OperatorMetadata;
import org.esa.beam.framework.gpf.annotations.Parameter;
import org.esa.beam.framework.gpf.annotations.SourceProduct;
import org.esa.beam.framework.gpf.annotations.SourceProducts;
import org.esa.beam.framework.gpf.annotations.TargetProduct;
import org.esa.beam.framework.gpf.annotations.TargetProperty;
import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings({"UnusedDeclaration"})
public class OperatorClassDescriptorTest {

    @Test
    public void testAnnotationParsing() {
        final OperatorClassDescriptor descriptor = new OperatorClassDescriptor(ParsedOperator.class);
        assertNotNull(descriptor.getOperatorMetadata());
        assertNotNull(descriptor.getParameters());
        assertEquals(2, descriptor.getParameters().size());
        assertNotNull(descriptor.getSourceProductMap());
        assertEquals(2, descriptor.getSourceProductMap().size());
        assertNotNull(descriptor.getSourceProducts());
        assertNotNull(descriptor.getTargetProduct());
        assertNotNull(descriptor.getTargetProperties());
        assertEquals(1, descriptor.getTargetProperties().size());
    }

    @OperatorMetadata(alias="Alice", version = "1.2.99")
    private static class ParsedOperator extends Operator {
        @Parameter(alias = "number")
        private int aNumber;
        @Parameter(alias = "name")
        private String text;

        @SourceProducts(count = -1)
        private Product[] sProducts;

        @SourceProduct
        private Product prod;

        @SourceProduct(alias = "another")
        private Product prod2;

        @TargetProduct(description = "Describing the output")
        private Product targetProd;

        @TargetProperty(alias = "prop", description = "Describing the output")
        private Product targetProperty;

        @Override
        public void initialize() throws OperatorException {
        }
    }
}
