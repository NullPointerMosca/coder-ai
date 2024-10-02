package it.dueirg.coderai.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.ericsson.urm.device.manager.MMDriverWrapper;

public class CommandDriverParameters implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int accNumber = -1;
	
	private Long frameCounter = null;
	
	private Long umiOpk = null;
	
	private String ekDecrypt = null;
	
	private MMDriverWrapper	mmDriverWrapper = null;
	
	private String wmBussAddress = null;
	
	private String systemTitleUniTs = null;
	
	public String getSystemTitleUniTs() {
		return systemTitleUniTs;
	}

	public void setSystemTitleUniTs(String systemTitleUniTs) {
		this.systemTitleUniTs = systemTitleUniTs;
	}

	public String getWmBussAddress() {
		return wmBussAddress;
	}

	public void setWmBussAddress(String wmBussAddress) {
		this.wmBussAddress = wmBussAddress;
	}

	public int getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(int accNumber) {
		this.accNumber = accNumber;
	}

	public Long getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(Long frameCounter) {
		this.frameCounter = frameCounter;
	}

	public Long getUmiOpk() {
		return umiOpk;
	}

	public void setUmiOpk(Long umiOpk) {
		this.umiOpk = umiOpk;
	}

	public MMDriverWrapper getMmDriverWrapper() {
		return mmDriverWrapper;
	}

	public void setMmDriverWrapper(MMDriverWrapper mmDriverWrapper) {
		this.mmDriverWrapper = mmDriverWrapper;
	}

	public String getEkDecrypt() {
		return ekDecrypt;
	}

	public void setEkDecrypt(String ekDecrypt) {
		this.ekDecrypt = ekDecrypt;
	}

	public CommandDriverParameters(int accNumber, Long frameCounter, Long umiOpk, MMDriverWrapper mmDriverWrapper, String wmBussAddress, String ekDecrypt, String systemTitleUniTs) {
		super();
		this.accNumber = accNumber;
		this.frameCounter = frameCounter;
		this.umiOpk = umiOpk;
		this.mmDriverWrapper = mmDriverWrapper;
		this.wmBussAddress = wmBussAddress;
		this.ekDecrypt = ekDecrypt;
		this.systemTitleUniTs = systemTitleUniTs;
	}

	public CommandDriverParameters(MMDriverWrapper mmDriverWrapper, String wmBussAddress) {
		super();
		this.mmDriverWrapper = mmDriverWrapper;
		this.wmBussAddress = wmBussAddress;
	}

	@Override
	public String toString() {
		ReflectionToStringBuilder tsb = new ReflectionToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		return tsb.toString();
	}

}
