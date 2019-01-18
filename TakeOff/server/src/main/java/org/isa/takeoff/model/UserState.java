package org.isa.takeoff.model;

import java.util.List;

public class UserState 
{	
    private String accessToken;
    private String username;
    private boolean isEnabled;
    private List<String> authorities;

    public UserState() { }

    public UserState(String accessToken, String username, boolean isEnabled, List<String> authorities) {
		this.accessToken = accessToken;
		this.username = username;
		this.isEnabled = isEnabled;
		this.authorities = authorities;
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

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
}