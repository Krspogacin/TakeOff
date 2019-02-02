package org.isa.takeoff.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;

@Entity
public class Friend implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FriendId id;

	@Column(name = "accepted")
	private boolean accepted;

	@Version
	private Long version;

	public Friend() {

	}

	public Friend(User user1, User user2) {
		this.id = new FriendId(user1, user2);
		this.accepted = false;
	}

	public FriendId getId() {
		return id;
	}

	public void setId(FriendId id) {
		this.id = id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Friend that = (Friend) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

}
