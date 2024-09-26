package it.dueirg.coderai.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MM_PROPERTIES")
public class MmProperties {

	@Id
	@Column(name = "PROP_NAME", nullable = false, length = 100)
	private String propName;

	@Column(name = "DESCRIPTION", length = 100)
	private String description;

	@Column(name = "PROP_TYPE", length = 200)
	private String propType;

	@Column(name = "PROP_VALUE", length = 200)
	private String propValue;
}