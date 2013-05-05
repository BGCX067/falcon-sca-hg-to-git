package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import java.util.*;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.DisplayUtils;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.fighterform.FighterForm;
import org.sca.calontir.cmpe.client.ui.fighterform.ScaNameField;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.*;

/**
 * This class displays the fighter view/edit/add page. It contains the logic to submit the form itself and handle the
 * response. It will also inform listeners if the data is changed.
 *
 * @author rikscarborough
 */
public class FighterFormWidget extends Composite implements EditViewHandler, FormPanel.SubmitHandler, FormPanel.SubmitCompleteHandler {

	private static final Logger log = Logger.getLogger(FighterFormWidget.class.getName());
	final private Security security = SecurityFactory.getSecurity();
	private Panel overallPanel = new FlowPanel();
	private Panel fighterIdBoxPanel = new FlowPanel();
	final private Hidden mode = new Hidden("mode");
	private Panel authPanel = new FlowPanel();
	private Panel infoPanel = new FlowPanel();
	private Panel notePanel = new FlowPanel();
	private Fighter fighter;
	private LookupController lookupController = LookupController.getInstance();
	private FormPanel form = null;

	private class Flag {

		protected boolean value = false;
	}

	private enum Target {

		Info, Auths
	};

	private enum DisplayMode {

		view, edit, add
	}

	public FighterFormWidget() {
		addHandler(this, EditViewEvent.TYPE);
		fighterIdBoxPanel.setStyleName("figherIdBox");
		overallPanel.add(fighterIdBoxPanel);

		authPanel.setStyleName("dataBox");
		overallPanel.add(authPanel);

		infoPanel.setStyleName("dataBox");
		overallPanel.add(infoPanel);

		notePanel.setStyleName("dataBoxShort");
		notePanel.getElement().getStyle().setDisplay(Style.Display.NONE);
		overallPanel.add(notePanel);


		initWidget(overallPanel);
	}

	@Override
	public void buildView() {
		buildInfoView();

		buildAuthView();
		DisplayUtils.changeDisplay(DisplayUtils.Displays.FighterForm, true);
	}

	@Override
	public void buildAdd() {
		fighterIdBoxPanel.clear();

		final Hidden fighterId = new Hidden("fighterId");
		fighterId.setValue("0");
		fighterIdBoxPanel.add(fighterId);

		mode.setValue("add");
		fighterIdBoxPanel.add(mode);

		fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));
		fighterIdBoxPanel.add(new ScaNameField(fighter, true));

		buildInfoPanel(DisplayMode.add);
		buildAuthEdit(false);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			buildNotePanel(true);
		}

		DisplayUtils.changeDisplay(DisplayUtils.Displays.FighterForm, true);
	}

	public void setForm(FormPanel form) {
		this.form = form;
	}

	// TODO: All fields should update the local fighter variable if they change
	// on both edit and add.
	// Consider creating each object as a sperate widget to enclose the functionality.
	public void buildInfoEdit() {
		fighterIdBoxPanel.clear();

		final Hidden fighterId = new Hidden("fighterId");
		fighterId.setValue(fighter.getFighterId().toString());
		fighterIdBoxPanel.add(fighterId);

		fighterIdBoxPanel.add(mode);


		fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));
		fighterIdBoxPanel.add(new ScaNameField(fighter, true));

		buildInfoPanel(DisplayMode.edit);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			buildNotePanel(true);
		}

	}

	public void buildAuthEdit(boolean edit) {
		authPanel.clear();

		Panel dataHeader = new FlowPanel();
		dataHeader.setStyleName("dataHeader");
		InlineLabel authLabel = new InlineLabel("Authorizations");
		dataHeader.add(authLabel);

		if (edit) {
			Panel editButton = new FlowPanel();
			editButton.setStyleName("editButton");
			editButton.getElement().getStyle().setDisplay(Style.Display.INLINE);
			editButton.add(saveButton(Target.Auths));
			editButton.add(cancelButton(Target.Auths));
			dataHeader.add(editButton);
		}

		authPanel.add(dataHeader);

		Panel dataBody = new FlowPanel();
		dataBody.setStyleName("dataBody");
		for (AuthType at : lookupController.getAuthType()) {
			final CheckBox cb = new CheckBox(at.getCode());
			cb.setFormValue(at.getCode());
			cb.setName("authorization");
			if (edit) {
				for (Authorization a : fighter.getAuthorization()) {
					if (a.getCode().equals(at.getCode())) {
						cb.setValue(true);
						break;
					}
				}
			}
			cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					List<Authorization> auths = fighter.getAuthorization();
					if (auths == null) {
						auths = new ArrayList<Authorization>();
						fighter.setAuthorization(auths);
					}
					Authorization a = new Authorization();
					a.setCode(cb.getFormValue());
					if (auths.contains(a)) {
						if (event.getValue()) {
							auths.add(a);
						} else {
							auths.remove(a);
						}
					} else {
						if (event.getValue()) {
							auths.add(a);
						}
					}
				}
			});
			dataBody.add(cb);
		}
		authPanel.add(dataBody);
	}

	public void buildAuthView() {
		authPanel.clear();

		Panel dataHeader = new FlowPanel();
		dataHeader.setStyleName("dataHeader");
		InlineLabel authLabel = new InlineLabel("Authorizations");
		dataHeader.add(authLabel);


		if (security.canEditAuthorizations(fighter.getFighterId())) {
			if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
				Panel editButton = new FlowPanel();
				editButton.setStyleName("editButton");
				editButton.getElement().getStyle().setDisplay(Style.Display.INLINE);
				editButton.add(editButton(Target.Auths));
				dataHeader.add(editButton);
			}
		}

		authPanel.add(dataHeader);

		Panel dataBody = new FlowPanel();
		dataBody.setStyleName("dataBody");
		Label auths = new Label();
		auths.setText(getAuthsAsString(fighter.getAuthorization()));
		dataBody.add(auths);
		authPanel.add(dataBody);
	}

	public void buildInfoView() {
		fighterIdBoxPanel.clear();

		final Hidden fighterId = new Hidden("fighterId");
		fighterId.setValue(fighter.getFighterId() == null ? "0" : fighter.getFighterId().toString());
		fighterIdBoxPanel.add(fighterId);
		fighterIdBoxPanel.add(mode);


		fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));
		fighterIdBoxPanel.add(new ScaNameField(fighter, false));

		if (this.fighter.getStatus().equals(FighterStatus.ACTIVE)) {
			fighterIdBoxPanel.add(printButton());
		}

		buildInfoPanel(DisplayMode.view);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			buildNotePanel(false);
		}

	}

	private void buildInfoPanel(DisplayMode dMode) {
		infoPanel.clear();

		Panel dataHeader = new FlowPanel();
		dataHeader.setStyleName("dataHeader");
		InlineLabel fighterLabel = new InlineLabel("Fighter Info");
		dataHeader.add(fighterLabel);

		boolean edit = false;
		Panel editButton = new FlowPanel();
		editButton.setStyleName("editButton");
		editButton.getElement().getStyle().setDisplay(Style.Display.INLINE);
		switch (dMode) {
			case edit:
				edit = true;
				editButton.add(saveButton(Target.Info));
				editButton.add(cancelButton(Target.Info));
				break;
			case view:
				if (security.canEditFighter(fighter.getFighterId())) {
					if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
						editButton.add(editButton(Target.Info));
					}
				}
				break;
			case add:
				edit = true;
			default:
		}
		dataHeader.add(editButton);

		infoPanel.add(dataHeader);

		Panel dataBody = new FlowPanel();
		dataBody.setStyleName("dataBody");

		Panel fighterInfo = new FlowPanel();
		fighterInfo.getElement().setId("fighterInfo");
		DOM.setElementAttribute(fighterInfo.getElement(), "id", "fighterInfo");
		dataBody.add(fighterInfo);

		fighterInfo.add(new FighterForm(fighter, edit, dMode == DisplayMode.add));

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) { // or user admin
			fighterInfo.add(adminInfo(edit));
		}

		infoPanel.add(dataBody);

		if (dMode == DisplayMode.add) {
			SubmitButton addFighter = new SubmitButton("Add Fighter");
			infoPanel.add(addFighter);
		}
	}

	private Panel adminInfo(boolean edit) {
		Panel adminInfo = new FlowPanel();
		DOM.setElementAttribute(adminInfo.getElement(), "id", "adminInfo");

		FlexTable adminTable = new FlexTable();
		FlexTable.FlexCellFormatter adminFormatter = adminTable.getFlexCellFormatter();

		adminTable.setText(0, 0, "Google ID:");
		if (edit) {
			final TextBox googleId = new TextBox();
			googleId.setName("googleId");
			googleId.setVisibleLength(25);
			googleId.setStyleName("googleId");
			googleId.setValue(fighter.getGoogleId());
			googleId.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					fighter.setGoogleId(googleId.getValue());
				}
			});
			adminTable.setWidget(0, 1, googleId);
		} else {
			adminTable.setText(0, 1, fighter.getGoogleId());
		}
		adminFormatter.setStyleName(0, 0, "label");
		adminFormatter.setStyleName(0, 1, "data");

		adminTable.setText(1, 0, "User Role:");
		if (edit) {
			final ListBox userRole = new ListBox();
			userRole.setName("userRole");
			for (UserRoles ur : UserRoles.values()) {
				userRole.addItem(ur.toString(), ur.toString());
			}

			if (fighter.getRole() != null) {
				for (int i = 0; i < userRole.getItemCount(); ++i) {
					if (userRole.getValue(i).equals(fighter.getRole().toString())) {
						userRole.setSelectedIndex(i);
						break;
					}
				}
			}
			userRole.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					fighter.setRole(UserRoles.valueOf(userRole.getValue(userRole.getSelectedIndex())));
				}
			});
			adminTable.setWidget(1, 1, userRole);
		} else {
			if (fighter.getRole() != null) {
				adminTable.setText(1, 1, fighter.getRole().toString());
			}
		}
		adminFormatter.setStyleName(1, 0, "label");
		adminFormatter.setStyleName(1, 1, "data");

		adminInfo.add(adminTable);

		return adminInfo;
	}

	private void buildNotePanel(boolean edit) {
		notePanel.clear();
		if (security.isRoleOrGreater(UserRoles.DEPUTY_EARL_MARSHAL)) {
			notePanel.getElement().getStyle().setDisplay(Style.Display.BLOCK);

			final Panel dataHeader = new FlowPanel();
			dataHeader.setStyleName("dataHeader");
			final InlineLabel label = new InlineLabel("Notes");
			dataHeader.add(label);

			notePanel.add(dataHeader);

			final Panel dataBody = new FlowPanel();
			dataBody.setStyleName("dataBody");
			final Hidden noteField = new Hidden();
			noteField.setID("notes");
			noteField.setName("notes");
			if (fighter.getNote() != null) {
				noteField.setValue(fighter.getNote().getBody());
			}
			notePanel.add(noteField);

			final RichTextArea noteTa = new RichTextArea();
			//noteTa.setName("notes");
			noteTa.setEnabled(edit);
			if (fighter.getNote() != null) {
				noteTa.setHTML(fighter.getNote().getBody());
			}
			noteTa.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
					RichTextArea source = (RichTextArea) event.getSource();
					String changed = source.getHTML();
					if (changed.isEmpty()) {
						fighter.setNote(null);
						noteField.setValue("");
					} else {
						Note note;
						if (fighter.getNote() != null) {
							note = fighter.getNote();
						} else {
							note = new Note();
						}
						note.setBody(changed);
						note.setUpdated(new Date());

						fighter.setNote(note);
						noteField.setValue(changed);
					}
				}
			});
			dataBody.add(noteTa);
			notePanel.add(dataBody);
		}
	}

	@Override
	public void setFighter(Fighter fighter) {
		this.fighter = fighter;
	}

	private Widget printButton() {
		final Anchor bPrint = new Anchor("Download Fighter Card");
		bPrint.setStyleName("buttonLink");

		bPrint.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bPrint.setEnabled(false);
				mode.setValue("printFighter");
				//form.setAction("/ServePDF.groovy");
				form.submit();
			}
		});

		return bPrint;
	}

	private Widget editButton(final Target target) {
		final Anchor editButton = new Anchor("edit");
		editButton.addStyleName("buttonLink");
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editButton.setEnabled(false);
				switch (target) {
					case Auths:
						buildInfoView();
						buildAuthEdit(true);
						break;
					case Info:
						buildAuthView();
						buildInfoEdit();
						break;
				}
			}
		});


		return editButton;
	}

	private Widget cancelButton(final Target target) {
		final Anchor cancelButton = new Anchor("cancel");
		cancelButton.addStyleName("buttonLink");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelButton.setEnabled(false);
				switch (target) {
					case Auths:
						buildAuthView();
						break;
					case Info:
						buildInfoView();
						break;
				}
			}
		});


		return cancelButton;
	}

	private Widget saveButton(final Target target) {
		final Anchor saveButton = new Anchor("save");
		saveButton.addStyleName("buttonLink");
		saveButton.getElement().getStyle().setMarginRight(1.5, Style.Unit.EM);
		saveButton.addClickHandler(new ClickHandler() {
			private boolean unclicked = true;

			@Override
			public void onClick(ClickEvent event) {
				if (unclicked) {
					unclicked = false;
					saveButton.setEnabled(false);
					switch (target) {
						case Auths:
							mode.setValue("saveAuthorizations");
							////form.setAction("/FighterServlet");
							form.submit();
							break;
						case Info:
							mode.setValue("saveFighter");
							//form.setAction("/FighterServlet");
							form.submit();
							break;
					}
				}
			}
		});


		return saveButton;
	}

	@Override
	public void onSubmit(SubmitEvent event) {
		StringBuilder errors = new StringBuilder();
		if (fighter.getScaName() == null || fighter.getScaName().trim().length() == 0) {
			errors.append("SCA Name cannot be blank");
		}
		if (fighter.getScaGroup() == null || fighter.getScaGroup().getGroupName().equalsIgnoreCase("SELECTGROUP")) {
			if (errors.length() > 0) {
				errors.append("\n");
			}
			errors.append("Please choose a group");
		}
		if (fighter.getAddress() == null || fighter.getAddress().isEmpty()) {
			errors.append("An address is required");
		}


		if (errors.length() > 0) {
			Window.alert("Errors, please fix\n" + errors.toString());
			event.cancel();
		}
		// TODO: create fighter out of form and validate.  Fighter used on add to refill page.
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		if (fighter != null && fighter.getFighterId() != null && fighter.getFighterId() > 0) {
			FighterServiceAsync fighterService = GWT.create(FighterService.class);
			fighterService.getFighter(fighter.getFighterId(), new AsyncCallback<Fighter>() {
				@Override
				public void onFailure(Throwable caught) {
					log.severe("Submit fighter form: " + caught.getMessage());
				}

				@Override
				public void onSuccess(Fighter result) {
					fireEvent(new DataUpdatedEvent(result));
					fireEvent(new EditViewEvent(Mode.VIEW, result));
				}
			});
		} else {
			final Timer t = new Timer() {
				String scaName = null;

				Timer setScaName(String scaName) {
					this.scaName = scaName;
					return this;
				}

				@Override
				public void run() {
					FighterServiceAsync fighterService = GWT.create(FighterService.class);
					fighterService.getFighterByScaName(scaName, new AsyncCallback<Fighter>() {
						@Override
						public void onFailure(Throwable caught) {
							log.severe("getFighterByScaName " + caught.getMessage());
						}

						@Override
						public void onSuccess(Fighter result) {
							if(result == null) {
								Shout.getInstance().tell("Error saving this record. Please retry later.");
							} else {
								fireEvent(new DataUpdatedEvent(result));
								fireEvent(new EditViewEvent(Mode.VIEW, result));
							}
						}
					});
				}
			}.setScaName(fighter.getScaName());
			fireEvent(new EditViewEvent(Mode.VIEW, fighter));

			t.schedule(500);
		}
	}

	private String getAuthsAsString(final List<Authorization> authorizations) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		if (authorizations != null) {
			Collections.sort(authorizations, new Comparator<Authorization>() {
				@Override
				public int compare(Authorization l, Authorization r) {
					long left = l.getOrderValue() == null ? 99 : l.getOrderValue();
					long right = r.getOrderValue() == null ? 99 : r.getOrderValue();
					return (left < right ? -1 : (left == right ? 0 : 1));
				}
			});
			for (Authorization a : authorizations) {
				if (first) {
					first = false;
				} else {
					sb.append(" ; ");
				}
				sb.append(a.getCode());
			}
		}

		return sb.toString();
	}
}
