package com.universer.HustWhereToEat.model;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "restaurant")
public class Restaurant extends BaseDaoEnabled<Restaurant, String> {
	@DatabaseField(generatedId = false)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String imageUrl;
	@DatabaseField
	private String address;
	@DatabaseField
	private String phone;
	// comment去掉
	@DatabaseField
	private String comment;
	@DatabaseField
	private boolean isLike;
	private List<String> commentList;// 应该是个List集合 这里先不做这个处理

	public Restaurant(String id, String name, String imageUrl, String address,
			String phone, boolean isLike, List<String> commentList) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.address = address;
		this.phone = phone;
		this.isLike = isLike;
		this.setCommentList(commentList);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<String> commentList) {
		this.commentList = commentList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	// public void setPoint(GeoPoint geoPoint) {
	// this.point = geoPoint;
	// }

}
