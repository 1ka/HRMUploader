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

import java.util.ArrayList;

public class Item {

	private String type;

	private String index;

	private final ArrayList<Property> props = new ArrayList<Property>();

	private ArrayList<PolarObject> polarObjects = new ArrayList<PolarObject>();

	private ArrayList<Collection> collections = new ArrayList<Collection>();

	public Item() {
		super();
	}

	public Item(final String type) {
		super();
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public ArrayList<Property> getProps() {
		return props;
	}

	public ArrayList<PolarObject> getPolarObjects() {
		return polarObjects;
	}

	public void setPolarObjects(final ArrayList<PolarObject> objs) {
		polarObjects = objs;
	}

	public ArrayList<Collection> getCollections() {
		return collections;
	}

	public void setCollections(final ArrayList<Collection> colls) {
		collections = colls;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(final String index) {
		this.index = index;
	}

}
