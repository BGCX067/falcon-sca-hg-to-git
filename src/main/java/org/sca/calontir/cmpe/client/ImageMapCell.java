package org.sca.calontir.cmpe.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import java.util.Map;

/**
 *
 * @author rikscarborough
 */
public class ImageMapCell extends AbstractCell<Map> {

    interface Template extends SafeHtmlTemplates {

        @Template("<img src=\"{0}\" usemap=\"#{1}\" /> "
                + "<map name=\"{1}\" id=\"{1}\" > "
                + " <area shape=\"circle\" coords=\"{2}\" /> "
                + "</map>")
        SafeHtml img(String url, String usemap, String coords);
    }
    private static Template template;

    public ImageMapCell() {
        if (template == null) {
            template = GWT.create(Template.class);
        }
    }

    @Override
    public void render(Context context, Map value, SafeHtmlBuilder sb) {
        if (value != null) {
            String url = (String) value.get("url");
            String usemap = (String) value.get("usemap");
            String coords = (String) value.get("coords");
            sb.append(template.img(url, usemap, coords));
        }
    }
}
