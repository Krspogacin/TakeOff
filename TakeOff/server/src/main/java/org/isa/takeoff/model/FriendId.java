package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FriendId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_1_id", referencedColumnName = "id")
	private User user1;

	@ManyToOne
	@JoinColumn(name = "user_2_id", referencedColumnName = "id")
	private User user2;

	public FriendId() {

	}

	public FriendId(User user1, User user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FriendId that = (FriendId) o;
		return Objects.equals(getUser1(), that.getUser1()) && Objects.equals(getUser2(), that.getUser2());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUser1(), getUser2());
	}
}
