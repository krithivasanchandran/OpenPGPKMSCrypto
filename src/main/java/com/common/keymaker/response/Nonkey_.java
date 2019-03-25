package com.common.keymaker.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"encode_format",
"exportable_to_app",
"encrypted",
"valid_to",
"name",
"attributes",
"state",
"version",
"encoded_key_data"
})
public class Nonkey_ {
	
	@JsonProperty("encode_format")
	private String encodeFormat;
	@JsonProperty("exportable_to_app")
	private Boolean exportableToApp;
	@JsonProperty("encrypted")
	private Boolean encrypted;
	@JsonProperty("valid_to")
	private String validTo;
	@JsonProperty("name")
	private String name;
	@JsonProperty("attributes")
	private String attributes;
	@JsonProperty("state")
	private String state;
	@JsonProperty("version")
	private Integer version;
	@JsonProperty("encoded_key_data")
	private String encodedKeyData;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("encode_format")
	public String getEncodeFormat() {
	return encodeFormat;
	}

	@JsonProperty("encode_format")
	public void setEncodeFormat(String encodeFormat) {
	this.encodeFormat = encodeFormat;
	}

	@JsonProperty("exportable_to_app")
	public Boolean getExportableToApp() {
	return exportableToApp;
	}

	@JsonProperty("exportable_to_app")
	public void setExportableToApp(Boolean exportableToApp) {
	this.exportableToApp = exportableToApp;
	}

	@JsonProperty("encrypted")
	public Boolean getEncrypted() {
	return encrypted;
	}

	@JsonProperty("encrypted")
	public void setEncrypted(Boolean encrypted) {
	this.encrypted = encrypted;
	}

	@JsonProperty("valid_to")
	public String getValidTo() {
	return validTo;
	}

	@JsonProperty("valid_to")
	public void setValidTo(String validTo) {
	this.validTo = validTo;
	}

	@JsonProperty("name")
	public String getName() {
	return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
	this.name = name;
	}

	@JsonProperty("attributes")
	public String getAttributes() {
	return attributes;
	}

	@JsonProperty("attributes")
	public void setAttributes(String attributes) {
	this.attributes = attributes;
	}

	@JsonProperty("state")
	public String getState() {
	return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
	this.state = state;
	}

	@JsonProperty("version")
	public Integer getVersion() {
	return version;
	}

	@JsonProperty("version")
	public void setVersion(Integer version) {
	this.version = version;
	}

	@JsonProperty("encoded_key_data")
	public String getEncodedKeyData() {
	return encodedKeyData;
	}

	@JsonProperty("encoded_key_data")
	public void setEncodedKeyData(String encodedKeyData) {
	this.encodedKeyData = encodedKeyData;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
	
	public String toString(){
		
	  StringBuilder builder = new StringBuilder();
	  builder.append("encodeFormat :: " + encodeFormat).append("\n");
	  builder.append("exportable_to_app  :: " + exportableToApp).append("\n");
	  builder.append("encrypted :: "+ encrypted).append("\n");
	  builder.append("validTo ::" + validTo).append("\n");
	  builder.append("name app :: " + name).append("\n");
	  builder.append("attributes  :: "+ attributes).append("\n");
	  builder.append("state :: "+ state).append("\n");
	  builder.append("version :: " + version).append("\n");
	  builder.append("encodedKeyData ::" + encodedKeyData).append("\n");
	  
	  return builder.toString();
	}

}