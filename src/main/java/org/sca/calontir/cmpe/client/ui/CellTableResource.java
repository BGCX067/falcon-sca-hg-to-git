/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;

/**
 *
 * @author rikscarborough
 */
public interface CellTableResource extends Resources {
	public CellTable.Resources INSTANCE =
               GWT.create(CellTableResource.class);

       /**
        * The styles used in this widget.
        */
       @Source("CellTable.css")
       CellTable.Style cellTableStyle();

	
}
