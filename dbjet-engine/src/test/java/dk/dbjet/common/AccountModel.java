package dk.dbjet.common;

import java.sql.Timestamp;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnIsIndex;
import dk.dbjet.annotation.JetColumnIsPrimary;
import dk.dbjet.annotation.JetColumnIsUnique;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.annotation.JetColumnUpdatable;
import dk.dbjet.annotation.JetColumnValue;
import dk.dbjet.annotation.JetTable;
import dk.dbjet.marshaller.JetBase64Marshaller;

@JetTable(name = "TEST_ACCOUNT_TABLE")
public class AccountModel implements JetModel {
	
	@JetColumn(name = "UID")
	@JetColumnIsPrimary
	private String id;
	
	@JetColumn(name = "NAME")
	@JetColumnUpdatable	
	private String name;
	
	@JetColumn(name = "EMAIL")
	@JetColumnUpdatable
	@JetColumnIsIndex
	@JetColumnIsUnique
	private String email;
	
	@JetColumn(name = "KEY_BASE64", marshaller = JetBase64Marshaller.class)
	@JetColumnUpdatable	
	private String key;
	
	@JetColumn(name = "SALARY", type = JetColumnType.FLOAT)
	private Float salary;
	
	@JetColumn(name = "AGE", type = JetColumnType.INTEGER)
	private Integer age;	
	
	@JetColumn(name = "LAST_UPDATE", type = JetColumnType.TIMESTAMP, defaultValue = JetColumnValue.CURRENT_TIMESTAMP_ON_UPDATE)
	private Timestamp lastUpdateTimestamp;
	
	@JetColumn(name = "AVATAR", type = JetColumnType.BINARY)
	private byte[] avatar;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	public byte[] getAvatar() {
		return avatar;
	}
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
