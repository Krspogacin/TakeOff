package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Friend implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_1_id", referencedColumnName = "id")
	private User user1;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_2_id", referencedColumnName = "id")
	private User user2;

	@Column(name = "accepted")
	private boolean accepted;

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

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Friend that = (Friend) o;
		return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user1, user2);
	}

}
