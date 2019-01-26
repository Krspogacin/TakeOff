package org.isa.takeoff.model;

public class UserState 
{	
    private String accessToken;
    private String username;
    private boolean isEnabled;
    private String authority;

    public UserState() { }

    public UserState(String accessToken, String username, boolean isEnabled, String authority) {
		this.accessToken = accessToken;
		this.username = username;
		this.isEnabled = isEnabled;
		this.authority = authority;
	}

	public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthorities(String authority) {
		this.authority = authority;
	}
}