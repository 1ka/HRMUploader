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
import java.util.ArrayList;

public class PolarObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5014784070094888353L;

	private String name;

	private final ArrayList<Property> props = new ArrayList<Property>();

	public PolarObject() {
		super();
	}

	public PolarObject(final String name) {
		super();
		this.name = name;
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

	public ArrayList<Property> getProps() {
		return props;
	}

}
