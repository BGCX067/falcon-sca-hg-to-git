package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class FighterForm extends Composite {

	private ModernName modernName;
	private StatusField statusField;
	private AddressField addressField;
	private TreatyField treatyField;
	private ScaMemberNoField scaMemberNoField;
	private MembershipExpiresField membershipExpiresField;
	private GroupField groupField;
	private MinorField minorField;
	private DateOfBirthField dateOfBirthField;
	private PhoneNumberField phoneNumberField;
	private EmailAddressField emailAddressField;

	public FighterForm(final Fighter fighter, final boolean edit, final boolean add) {
		modernName = new ModernName(fighter, edit);
		statusField = new StatusField(fighter, edit);
		addressField = new AddressField(fighter, edit);
		treatyField =  new TreatyField(fighter, edit);
		scaMemberNoField = new ScaMemberNoField(fighter, edit);
		membershipExpiresField = new MembershipExpiresField(fighter, edit);
		groupField = new GroupField(fighter, edit, add);
		minorField = new MinorField(fighter, edit);
		dateOfBirthField = new DateOfBirthField(fighter, edit);
		phoneNumberField = new PhoneNumberField(fighter, edit);
		emailAddressField = new EmailAddressField(fighter, edit);

		buildTable();
	}

	private void buildRow(FlexTable table, int row, String label, Widget... widgets) {
		FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
		table.setText(row, 0, label);
		formatter.setStyleName(row, 0, "label");

		int i = 1;
		for(Widget widget : widgets) {
			table.setWidget(row, i, widget);
			if(i < widgets.length) {
				formatter.setStyleName(row, i, "data");
			} else {
				formatter.setStyleName(row, i, "rightCol");
			}
			++i;
		}
	}

	private void buildTable() {
		FlexTable table = new FlexTable();
		FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();

		int row = 0;
		table.setStyleName("wide-table");

		buildRow(table, row++, "Modern Name:", modernName, statusField);
		buildRow(table, row++, "Address:", addressField, treatyField);
		buildRow(table, row++, "SCA Membership:", scaMemberNoField);
		buildRow(table, row++, "Membership Expiration Date:", membershipExpiresField);
		buildRow(table, row++, "Group:", groupField);
		buildRow(table, row++, "Minor:", minorField);
		buildRow(table, row++, "Date of Birth:", dateOfBirthField);
		buildRow(table, row++, "Phone Number:", phoneNumberField);
		buildRow(table, row++, "Email Address:", emailAddressField);

		initWidget(table);
	}

	public ModernName getModernName() {
		return modernName;
	}

	public void setModernName(ModernName modernName) {
		this.modernName = modernName;
	}

	public StatusField getStatusField() {
		return statusField;
	}

	public void setStatusField(StatusField statusField) {
		this.statusField = statusField;
	}

	public AddressField getAddressField() {
		return addressField;
	}

	public void setAddressField(AddressField addressField) {
		this.addressField = addressField;
	}

	public TreatyField getTreatyField() {
		return treatyField;
	}

	public void setTreatyField(TreatyField treatyField) {
		this.treatyField = treatyField;
	}

	public ScaMemberNoField getScaMemberNoField() {
		return scaMemberNoField;
	}

	public void setScaMemberNoField(ScaMemberNoField scaMemberNoField) {
		this.scaMemberNoField = scaMemberNoField;
	}

	public MembershipExpiresField getMembershipExpiresField() {
		return membershipExpiresField;
	}

	public void setMembershipExpiresField(MembershipExpiresField membershipExpiresField) {
		this.membershipExpiresField = membershipExpiresField;
	}

	public GroupField getGroupField() {
		return groupField;
	}

	public void setGroupField(GroupField groupField) {
		this.groupField = groupField;
	}

	public MinorField getMinorField() {
		return minorField;
	}

	public void setMinorField(MinorField minorField) {
		this.minorField = minorField;
	}

	public DateOfBirthField getDateOfBirthField() {
		return dateOfBirthField;
	}

	public void setDateOfBirthField(DateOfBirthField dateOfBirthField) {
		this.dateOfBirthField = dateOfBirthField;
	}

	public PhoneNumberField getPhoneNumberField() {
		return phoneNumberField;
	}

	public void setPhoneNumberField(PhoneNumberField phoneNumberField) {
		this.phoneNumberField = phoneNumberField;
	}

	public EmailAddressField getEmailAddressField() {
		return emailAddressField;
	}

	public void setEmailAddressField(EmailAddressField emailAddressField) {
		this.emailAddressField = emailAddressField;
	}
	
}
