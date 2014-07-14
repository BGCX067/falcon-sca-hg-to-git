package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;

/**
 *
 * @author rikscarborough
 */
public class ScaNameSuggestOracle extends SuggestOracle {

    @Override
    public void requestSuggestions(SuggestOracle.Request request, SuggestOracle.Callback callback) {
        FighterServiceAsync fighterService = GWT.create(FighterService.class);
        fighterService.suggestScaName(request, new ScaNameSuggest(request, callback));
    }

    class ScaNameSuggest implements AsyncCallback<SuggestOracle.Response> {

        private final SuggestOracle.Request req;
        private final SuggestOracle.Callback callback;

        public ScaNameSuggest(SuggestOracle.Request req, SuggestOracle.Callback callback) {
            this.req = req;
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            callback.onSuggestionsReady(req, new SuggestOracle.Response());
        }

        @Override
        public void onSuccess(SuggestOracle.Response result) {
            callback.onSuggestionsReady(req, result);
        }

    }
}
