package com.yfaney.hear;

public class UserAdminInfoModel extends UserInformationModel{
	private String organization;
	private String emailAddress;
	
	public UserAdminInfoModel(int ID, String firstName, String lastName,
			String userId, String passWord, String createdOn,
			String organization, String emailAddress) {
		super(ID, firstName, lastName, userId, passWord, createdOn);
		this.organization = organization;
		this.emailAddress = emailAddress;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


}
