package com.ixiangni.ui.ImageLabel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

public final class TagInfo implements Parcelable, Serializable {
	private static final long serialVersionUID = -2939266917839493174L;
	public String bname = "";
	public long bid = 0L;
	public double pic_x = 0.0D;		//点击点在图片中的位置，百分比
	public double pic_y = 0.0D;		//点击点在图片中的位置，百分比
	public Direction direct = Direction.Left;
	public Type type = Type.Undefined;
	
	public int leftMargin;
	public int topMargin;
	public int rightMargin;
	public int bottomMargin;


	public String tagid;

	@Override
	public String toString() {
		return "TagInfo{" +
				"bname='" + bname + '\'' +
				", bid=" + bid +
				", pic_x=" + pic_x +
				", pic_y=" + pic_y +
				", direct=" + direct +
				", type=" + type +
				", leftMargin=" + leftMargin +
				", topMargin=" + topMargin +
				", rightMargin=" + rightMargin +
				", bottomMargin=" + bottomMargin +
				", tagid='" + tagid + '\'' +
				'}';
	}

	public enum Direction {
		Left("left"), Right("right");
		
		private Direction(String valString) {
			this.valueString = valString;
		}
		
		public static int size(){
			return Direction.values().length;
		}
		
		public String valueString;
		
		public String toString(){
			return valueString;
		}
		
		public static Direction valueof(String vaString){
			if(vaString.equals("left")){
				return Direction.Left;
			}else if(vaString.equals("right")){
				return Direction.Right;
			}else{
				return null;
			}
		}
	}

	public enum Type {
		Undefined("undefined"),Exists("exists"),CustomPoint("custom_point"),OfficalPoint("offical_point");
		
		private Type(String typeString) {
			this.valueString = typeString;
		}
		
		public static int size(){
			return Type.values().length;
		}
		
		public String valueString;
		
		public String toString(){
			return valueString;
		}
		
		public static Type valueof(String vaString){
			if(vaString.equals("undefined")){
				return Type.Undefined;
			}else if(vaString.equals("exists")){
				return Type.Exists;
			}else if(vaString.equals("custom_point")){
				return Type.CustomPoint;
			}else if(vaString.equals("offical_point")){
				return Type.OfficalPoint;
			}else{
				return null;
			}
		}
	}

	public TagInfo() {
	}


	public TagInfo(JSONObject paramJSONObject) {
		String str = null;
		try {
			this.bid = paramJSONObject.getLong("bid");
			this.bname = paramJSONObject.getString("bname");
			this.pic_x = paramJSONObject.getDouble("pic_x");
			this.pic_y = paramJSONObject.getDouble("pic_y");
			this.direct = Direction.valueof(paramJSONObject.getString("direct"));
			if(null == direct){
				throw new RuntimeException("taginfo no direction");
			}
			this.type = Type.Undefined;
			if (!paramJSONObject.has("type")) {
				return;
			}
			str = paramJSONObject.getString("type");
			if (str.equals("exists")) {
				this.type = Type.Exists;
				return;
			}
			if (str.equals("custom_point")) {
				this.type = Type.CustomPoint;
				return;
			}
			if (str.equals("offical_point")) {
				this.type = Type.OfficalPoint;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TagInfo getInstance(JSONObject paramJSONObject) {
		return new TagInfo(paramJSONObject);
	}

	public final JSONObject getjson() {
		JSONObject jsonobject = new JSONObject();
		try {
			jsonobject.put("bid", bid);
			jsonobject.put("bname", bname);
			jsonobject.put("pic_x", String.valueOf(pic_x));
			jsonobject.put("pic_y", String.valueOf(pic_y));
			jsonobject.put("direct", direct.toString());
			jsonobject.put("type", type.toString());
			return jsonobject;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return jsonobject;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.bname);
		dest.writeLong(this.bid);
		dest.writeDouble(this.pic_x);
		dest.writeDouble(this.pic_y);
		dest.writeInt(this.direct == null ? -1 : this.direct.ordinal());
		dest.writeInt(this.type == null ? -1 : this.type.ordinal());
		dest.writeInt(this.leftMargin);
		dest.writeInt(this.topMargin);
		dest.writeInt(this.rightMargin);
		dest.writeInt(this.bottomMargin);
	}

	protected TagInfo(Parcel in) {
		this.bname = in.readString();
		this.bid = in.readLong();
		this.pic_x = in.readDouble();
		this.pic_y = in.readDouble();
		int tmpDirect = in.readInt();
		this.direct = tmpDirect == -1 ? null : Direction.values()[tmpDirect];
		int tmpType = in.readInt();
		this.type = tmpType == -1 ? null : Type.values()[tmpType];
		this.leftMargin = in.readInt();
		this.topMargin = in.readInt();
		this.rightMargin = in.readInt();
		this.bottomMargin = in.readInt();
	}

	public static final Creator<TagInfo> CREATOR = new Creator<TagInfo>() {
		@Override
		public TagInfo createFromParcel(Parcel source) {
			return new TagInfo(source);
		}

		@Override
		public TagInfo[] newArray(int size) {
			return new TagInfo[size];
		}
	};
}