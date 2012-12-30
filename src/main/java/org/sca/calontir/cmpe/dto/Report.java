package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author rikscarborough
 */
public class Report implements Serializable {
	private Long id;
	private String dateEntered;
	private String reportType;
	private String marshalName;
	private Long marshalId;
	private String googleId;

	private Map<String, String> reportParams;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(String dateEntered) {
		this.dateEntered = dateEntered;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getMarshalName() {
		return marshalName;
	}

	public void setMarshalName(String marshalName) {
		this.marshalName = marshalName;
	}

	public Long getMarshalId() {
		return marshalId;
	}

	public void setMarshalId(Long marshalId) {
		this.marshalId = marshalId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public Map<String, String> getReportParams() {
		return reportParams;
	}

	public void setReportParams(Map<String, String> reportParams) {
		this.reportParams = reportParams;
	}
}
