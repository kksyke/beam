package org.esa.beam.gpf.common.mosaic.ui;

import org.esa.beam.framework.gpf.ui.TargetProductSelector;
import org.esa.beam.framework.ui.AppContext;

import javax.swing.JTabbedPane;

/**
 * @author Marco Peters
 * @version $ Revision $ Date $
 * @since BEAM 4.7
 */
public class MosaicForm extends JTabbedPane {

    private final AppContext appContext;
    private MosaicFormModel mosaicModel;
    private MosaicIOPanel ioPanel;
    private MosaicMapProjectionPanel mapProjectionPanel;
    private MosaicExpressionsPanel expressionsPanel;

    public MosaicForm(TargetProductSelector targetProductSelector, AppContext appContext) {
        this.appContext = appContext;
        mosaicModel = new MosaicFormModel();
        createUI(targetProductSelector);
    }

    private void createUI(TargetProductSelector selector) {
        ioPanel = new MosaicIOPanel(appContext, mosaicModel, selector);
        mapProjectionPanel = new MosaicMapProjectionPanel(appContext, mosaicModel);
        expressionsPanel = new MosaicExpressionsPanel(appContext, mosaicModel);

        addTab("I/O Parameters", ioPanel); /*I18N*/
        addTab("Map Projection Definition", mapProjectionPanel); /*I18N*/
        addTab("Variables & Conditions", expressionsPanel);  /*I18N*/
    }


    void prepareShow() {
        mapProjectionPanel.prepareShow();
        // todo init source product selectors
//        sourceProductSelector.initProducts();
    }

    void prepareHide() {
        mapProjectionPanel.prepareHide();
        // todo release products of source product selectors
//        sourceProductSelector.releaseProducts();
    }

}
