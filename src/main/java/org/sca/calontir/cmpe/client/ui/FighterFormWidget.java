package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
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

		// Todo: move the login from calonbar to indexpage and do
		// before loading data. This will insure that security will
		// be correct before building the pages.
		notePanel.setStyleName("dataBoxShort");
		notePanel.getElement().getStyle().setDisplay(Style.Display.NONE);
		overallPanel.add(notePanel);


		initWidget(overallPanel);
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

		//move to inner class
		//add field value to fighter when user types.
		final TextBox scaNameTextBox = new TextBox();
		scaNameTextBox.setName("scaName");
		scaNameTextBox.getElement().setId("scaName");
		scaNameTextBox.setVisibleLength(25);
		scaNameTextBox.setStyleName("scaName");
		scaNameTextBox.setValue(fighter.getScaName());
		scaNameTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				//validate
				fighter.setScaName(event.getValue());
			}
		});


		fighterIdBoxPanel.add(scaNameTextBox);

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
					if(auths.contains(a)) {
						if(event.getValue()) {
							auths.add(a);
						} else {
							auths.remove(a);
						}
					} else {
						if(event.getValue()) {
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

		InlineLabel scaName = new InlineLabel();
		scaName.setText(fighter.getScaName());
		scaName.setStyleName("scaName");
		fighterIdBoxPanel.add(scaName);

		if (this.fighter.getStatus().equals(FighterStatus.ACTIVE)) {
			fighterIdBoxPanel.add(printButton());
		}

		buildInfoPanel(DisplayMode.view);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			buildNotePanel(false);
		}
	}

	@Override
	public void buildView() {
		buildInfoView();

		buildAuthView();
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

		FlexTable table = new FlexTable();
		FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
		table.setStyleName("wide-table");
		table.setText(0, 0, "Modern Name:");
		//if (dMode == DisplayMode.view) {
		if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			final TextBox modernName = new TextBox();
			modernName.setName("modernName");
			DOM.setElementAttribute(modernName.getElement(), "id", "modernName");
			modernName.setVisibleLength(25);
			modernName.setStyleName("modernName");
			modernName.setValue(fighter.getModernName());
			modernName.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					fighter.setModernName(modernName.getValue());
				}
			});
			table.setWidget(0, 1, modernName);
		} else {
			table.setText(0, 1, fighter.getModernName());
		}

		if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			final ListBox status = new ListBox();
			status.setName("fighterStatus");
			for (FighterStatus f_status : FighterStatus.values()) {
				status.addItem(f_status.toString(), f_status.toString());
			}

			for (int i = 0; i < status.getItemCount(); ++i) {
				if (status.getValue(i).equals(fighter.getStatus().toString())) {
					status.setSelectedIndex(i);
					break;
				}
			}
			status.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					fighter.setStatus(FighterStatus.valueOf(status.getValue(status.getSelectedIndex())));
				}
			});
			table.setWidget(0, 2, status);
		} else {
			table.setText(0, 2, fighter.getStatus().toString());
		}
		formatter.setStyleName(0, 0, "label");
		formatter.setStyleName(0, 1, "data");
		formatter.setStyleName(0, 2, "rightCol");

		table.setText(1, 0, "Address:");
		final Address address;
		if (fighter.getAddress() != null && !fighter.getAddress().isEmpty()) {
			address = fighter.getAddress().get(0);
		} else {
			address = new Address();
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			fighter.setAddress(addresses);
		}
		if (edit) {
			final FlexTable addressTable = new FlexTable();
			addressTable.setText(0, 0, "Street:");
			final TextBox address1 = new TextBox();
			address1.setName("address1");
			address1.setValue(address.getAddress1());
			address1.setVisibleLength(60);
			address1.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					address.setAddress1(address1.getValue());
				}
			});
			addressTable.setWidget(0, 1, address1);

			addressTable.setText(1, 0, "Line 2:");
			final TextBox address2 = new TextBox();
			address2.setName("address2");
			address2.setValue(address.getAddress2());
			address2.setVisibleLength(60);
			address2.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					address.setAddress2(address2.getValue());
				}
			});
			addressTable.setWidget(1, 1, address2);

			addressTable.setText(2, 0, "City:");
			final TextBox city = new TextBox();
			city.setName("city");
			city.setValue(address.getCity());
			city.setVisibleLength(30);
			city.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					address.setCity(city.getValue());
				}
			});
			addressTable.setWidget(2, 1, city);

			addressTable.setText(3, 0, "State:");
			final TextBox state = new TextBox();
			state.setName("state");
			state.setValue(address.getState());
			state.setVisibleLength(20);
			state.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					address.setState(state.getValue());
				}
			});
			addressTable.setWidget(3, 1, state);

			addressTable.setText(4, 0, "Postal Code:");
			final TextBox postalCode = new TextBox();
			postalCode.setName("postalCode");
			postalCode.setValue(address.getPostalCode());
			postalCode.setVisibleLength(30);
			postalCode.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					address.setPostalCode(postalCode.getValue());
				}
			});
			addressTable.setWidget(4, 1, postalCode);

			table.setWidget(1, 1, addressTable);
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(address.getAddress1());
			if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
				sb.append(", ");
				sb.append(address.getAddress2());
			}
			sb.append(", ");
			sb.append(address.getCity());
			sb.append(", ");
			sb.append(address.getState());
			sb.append("&nbsp;&nbsp;");
			sb.append(address.getPostalCode());
			table.setHTML(1, 1, sb.toString());
		}
		formatter.setStyleName(1, 0, "label");
		formatter.setStyleName(1, 1, "data");

		if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			final CheckBox treaty = new CheckBox("Treaty");
			treaty.setName("treaty");
			if (fighter.getTreaty() != null) {
				treaty.setValue(true);
			}
			treaty.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Treaty t = new Treaty();
					t.setName("Treaty");
					fighter.setTreaty(t);
				}
			});
			table.setWidget(1, 2, treaty);
		} else {
			if (fighter.getTreaty() != null) {
				table.setText(1, 2, "Treaty");
			}
		}
		formatter.setStyleName(1, 2, "rightCol");
		formatter.setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_TOP);
		table.setText(2, 0, "SCA Membership:");
		if (edit) {
			final TextBox scaMemberNo = new TextBox();
			scaMemberNo.setName("scaMemberNo");
			scaMemberNo.setVisibleLength(20);
			scaMemberNo.setStyleName("scaMemberNo");
			scaMemberNo.setValue(fighter.getScaMemberNo());
			scaMemberNo.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					fighter.setScaMemberNo(scaMemberNo.getValue());
				}
			});
			table.setWidget(2, 1, scaMemberNo);
		} else {
			table.setText(2, 1, fighter.getScaMemberNo());
		}
		formatter.setStyleName(2, 0, "label");
		formatter.setStyleName(2, 1, "data");

		table.setText(3, 0, "Group:");
		if (edit) {
			final ListBox group = new ListBox();
			group.setName("scaGroup");
			if (dMode == DisplayMode.add) {
				group.addItem("Select a group", "SELECTGROUP");
			}
			for (ScaGroup g : lookupController.getScaGroups()) {
				group.addItem(g.getGroupName(), g.getGroupName());
			}

			if (fighter.getScaGroup() != null) {
				for (int i = 0; i < group.getItemCount(); ++i) {
					if (group.getValue(i).equals(fighter.getScaGroup().getGroupName())) {
						group.setSelectedIndex(i);
						break;
					}
				}
			}
			group.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					for (ScaGroup g : lookupController.getScaGroups()) {
						if (g.getGroupName().equals(group.getValue(group.getSelectedIndex()))) {
							fighter.setScaGroup(g);
							break;
						}
					}
				}
			});
			table.setWidget(3, 1, group);
		} else {
			table.setText(3, 1,
					fighter.getScaGroup() == null ? "" : fighter.getScaGroup().getGroupName());
		}
		formatter.setStyleName(3, 0, "label");
		formatter.setStyleName(3, 1, "data");

		table.setText(4, 0, "Minor:");
		if (fighter.getDateOfBirth() != null) {
			table.setText(4, 1, isMinor(fighter.getDateOfBirth()) ? "true" : "false");
		} else {
			table.setText(4, 1, "false");
		}
		formatter.setStyleName(4, 0, "label");
		formatter.setStyleName(4, 1, "data");

		table.setText(5, 0, "DOB:");
		if (edit) {
			final DateBox dateOfBirth = new DateBox();
			dateOfBirth.getTextBox().getElement().setId("dateOfBirth");
			dateOfBirth.getTextBox().setName("dateOfBirth");
			dateOfBirth.setFormat(
					new DateBox.DefaultFormat(
					DateTimeFormat.getFormat("MM/dd/yyyy")));
			dateOfBirth.setStyleName("dateOfBirth");
			if (fighter.getDateOfBirth() != null) {
				dateOfBirth.setValue(fighter.getDateOfBirth());
			}
			dateOfBirth.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					fighter.setDateOfBirth(DateTimeFormat.getFormat("MM/dd/yyyy").parse(dateOfBirth.getTextBox().getValue()));
				}
			});
			table.setWidget(5, 1, dateOfBirth);
		} else if (fighter.getDateOfBirth() != null) {
			table.setText(5, 1,
					DateTimeFormat.getFormat("MM/dd/yyyy").format(fighter.getDateOfBirth()));
		}
		formatter.setStyleName(5, 0, "label");
		formatter.setStyleName(5, 1, "data");

		table.setText(6, 0, "Phone Number:");
		if (edit) {
			final TextBox phoneNumber = new TextBox();
			phoneNumber.setName("phoneNumber");
			phoneNumber.setVisibleLength(25);
			phoneNumber.setStyleName("phoneNumber");
			if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
				phoneNumber.setValue(fighter.getPhone().get(0).getPhoneNumber());
			}
			phoneNumber.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
						Phone phone = new Phone();
						phone.setPhoneNumber(phoneNumber.getValue());
						List<Phone> phones = new ArrayList<Phone>();
						phones.add(phone);
						fighter.setPhone(phones);
					} else {
						fighter.getPhone().get(0).setPhoneNumber(phoneNumber.getValue());
					}
				}
			});
			table.setWidget(6, 1, phoneNumber);
		} else {
			if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
				table.setText(6, 1, fighter.getPhone().get(0).getPhoneNumber());
			}
		}
		formatter.setStyleName(6, 0, "label");
		formatter.setStyleName(6, 1, "data");

		table.setText(7, 0, "Email Address:");
		if (edit) {
			final TextBox email = new TextBox();
			email.setName("email");
			email.setVisibleLength(25);
			email.setStyleName("email");
			if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
				email.setValue(fighter.getEmail().get(0).getEmailAddress());
			}
			email.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
						Email e = new Email();
						e.setEmailAddress(email.getValue());
						List<Email> emails = new ArrayList<Email>();
						emails.add(e);
						fighter.setEmail(emails);
					} else {
						fighter.getEmail().get(0).setEmailAddress(email.getValue());
					}
				}
			});
			table.setWidget(7, 1, email);
		} else {
			if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
				table.setText(7, 1, fighter.getEmail().get(0).getEmailAddress());
			}
		}
		formatter.setStyleName(7, 0, "label");
		formatter.setStyleName(7, 1, "data");

		fighterInfo.add(table);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) { // or user admin
			Panel adminInfo = new FlowPanel();
			adminInfo.getElement().setId("fighterInfo");
			DOM.setElementAttribute(adminInfo.getElement(), "id", "adminInfo");
			fighterInfo.add(adminInfo);

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

		}

		infoPanel.add(dataBody);

		if (dMode == DisplayMode.add) {
			SubmitButton addFighter = new SubmitButton("Add Fighter");
			infoPanel.add(addFighter);
		}
	}

	private void buildNotePanel(boolean edit) {
		notePanel.clear();
		if (security.isRole(UserRoles.CARD_MARSHAL)) {
			notePanel.getElement().getStyle().setDisplay(Style.Display.BLOCK);

			Panel dataHeader = new FlowPanel();
			dataHeader.setStyleName("dataHeader");
			InlineLabel label = new InlineLabel("Notes");
			dataHeader.add(label);

			notePanel.add(dataHeader);

			Panel dataBody = new FlowPanel();
			dataBody.setStyleName("dataBody");
			TextArea noteTa = new TextArea();
			noteTa.setName("notes");
			noteTa.setReadOnly(!edit);
			if (fighter.getNote() != null) {
				noteTa.setText(fighter.getNote().getBody());
			}
			dataBody.add(noteTa);
			notePanel.add(dataBody);
		}
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

		final TextBox scaNameTextBox = new TextBox();
		scaNameTextBox.setName("scaName");
		scaNameTextBox.setVisibleLength(25);
		scaNameTextBox.setStyleName("scaName");
		scaNameTextBox.setValue(fighter.getScaName());
		scaNameTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				//validate
				fighter.setScaName(event.getValue());
			}
		});
		fighterIdBoxPanel.add(scaNameTextBox);

		buildInfoPanel(DisplayMode.add);
		buildAuthEdit(false);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			buildNotePanel(true);
		}
	}

	@Override
	public void setFighter(Fighter fighter) {
		this.fighter = fighter;
	}

	private boolean isMinor(Date dob) {
		Date now = new Date();
		Date targetDate = new Date(dob.getYear() + 18, dob.getMonth(), dob.getDate());
		if (targetDate.after(now)) {
			return true;
		}
		return false;
	}

	private Widget printButton() {
		Anchor bPrint = new Anchor("Print Fighter Card");
		bPrint.setStyleName("BPrint");
		bPrint.getElement().setId("BPrint");

		bPrint.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mode.setValue("printFighter");
				//form.setAction("/ServePDF.groovy");
				form.submit();
			}
		});

		return bPrint;
	}

	private Widget editButton(final Target target) {
		Anchor editButton = new Anchor("edit");
//        editButton.setStyleName("button");
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
		Anchor cancelButton = new Anchor("cancel");
//        cancelButton.setStyleName("button");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
		Anchor saveButton = new Anchor("save");
		saveButton.setStyleName("button");
		saveButton.getElement().getStyle().setMarginRight(1.5, Style.Unit.EM);
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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
					throw new UnsupportedOperationException("Not supported yet.");
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
							throw new UnsupportedOperationException("Not supported yet.");
						}

						@Override
						public void onSuccess(Fighter result) {
							fireEvent(new DataUpdatedEvent(result));
							fireEvent(new EditViewEvent(Mode.VIEW, result));
						}
					});
				}
			}.setScaName(fighter.getScaName());
			fireEvent(new EditViewEvent(Mode.VIEW, fighter));

			t.schedule(500);
		}
	}

	private String getAuthsAsString(List<Authorization> authorizations) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		if (authorizations != null) {
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
