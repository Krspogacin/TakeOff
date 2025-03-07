package org.isa.takeoff.dto;

import org.isa.takeoff.model.Friend;

public class FriendDTO {

	private UserDTO user1;
	private UserDTO user2;
	private boolean accepted;
	private Long version;

	public FriendDTO() {

	}

	public FriendDTO(Friend friend) {
		this(new UserDTO(friend.getId().getUser1()), new UserDTO(friend.getId().getUser2()), friend.isAccepted(), friend.getVersion());
	}

	public FriendDTO(UserDTO user1, UserDTO user2, boolean accepted, Long version) {
		super();
		this.user1 = user1;
		this.user2 = user2;
		this.accepted = accepted;
		this.version = version;
	}

	public UserDTO getUser1() {
		return user1;
	}

	public void setUser1(UserDTO user1) {
		this.user1 = user1;
	}

	public UserDTO getUser2() {
		return user2;
	}

	public void setUser2(UserDTO user2) {
		this.user2 = user2;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
