package com.universer.HustWhereToEat.model;

import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "restaurant")
public class Restaurant extends BaseDaoEnabled<Restaurant, String> {

	public static int BIG = 0;
	public static int SMALL = 1;
	@DatabaseField(generatedId = true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String imageUrl;
	@DatabaseField
	private int type;
	@DatabaseField
	private String address;
	@DatabaseField
	private String phone;
	@DatabaseField
	private String comment;
	@DatabaseField
	private int latitude;
	@DatabaseField
	private int longtitude;
	private List<String> commentList;// 应该是个List集合 这里先不做这个处理
	private GeoPoint point;

	public GeoPoint getPoint() {
		return point;
	}

	public void setPoint(int latitude,int longtitude) {
		this.point = new GeoPoint(latitude, longtitude);
	}

	public Restaurant(String name, String imageUrl, int type, String address,
			String phone, List<String> commentList) {
		super();

		this.name = name;
		this.imageUrl = imageUrl;
		this.type = type;
		this.address = address;
		this.phone = phone;
		this.setCommentList(commentList);
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public void setPoint(GeoPoint geoPoint) {
		this.point = geoPoint;
	}

}
