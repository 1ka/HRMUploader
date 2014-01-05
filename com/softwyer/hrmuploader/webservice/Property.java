/**
 *  Copyright Softwyer Ltd. 2006-2013.  All rights reserved.
 * 
 *  This software is provided 'as is' without warranty of any kind, either express or implied, including, but not limited to, 
 *  the implied warranties of fitness for a purpose, or the warranty of non-infringement.
 *  
 *  This software can be freely distributed as long as this header remains intact.  
 *  
 *  It can be used in non-commercial projects as long as this raw source code is included in the distribution with this header intact. 
 *  
 *  http://softwyer.wordpress.org
 *  
 *  http://rikara.blogspot.com/2013/04/connecting-to-polar-personal-trainer.html
 *  
 */
package com.softwyer.hrmuploader.webservice;

import java.io.Serializable;

public class Property implements Serializable {

	private static final long serialVersionUID = -5522564607842907232L;

	private String type;

	private String name;

	private String value;

	public Property(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Property [type=" + type + ", name=" + name + ", value=" + value
				+ "]";
	}

}
