package vn.nganluong.naba.entities;
// Generated Nov 1, 2020 4:13:02 PM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PgConfiguration generated by hbm2java
 */
@Entity
@Table(name = "pg_configuration")
public class PgConfiguration implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3504187991386119038L;
	private Integer id;
	private String code;
	private String value;
	private byte status;

	public PgConfiguration() {
	}

	public PgConfiguration(String code, String value, byte status) {
		this.code = code;
		this.value = value;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "code", nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "value", nullable = false)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
