package com.prova.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseModel {

	@Column(name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;

	@Column(name = "update_date", insertable = false)
	private Date updateDate;

	@PreUpdate
	protected void onUpdate() {
		updateDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

}
