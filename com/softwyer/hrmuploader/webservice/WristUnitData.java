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

public class WristUnitData implements Serializable {

	private static final long serialVersionUID = 5625814706408466389L;

	public static final String KEY_STATUS = "status";
	public static final String KEY_USER_PREFERENCES = "userPreferences";

	public WristUnitData() {

	}

	public WristUnitData(final PolarObject userSettings,
			final Collection exercises, final Collection tests,
			final ArrayList<Property> properties) {

		this.userSettings.add(userSettings);

		collection[0] = exercises;
		collection[1] = tests;

		this.properties = properties;

	}

	private ArrayList<Property> properties = new ArrayList<Property>();

	private ArrayList<PolarObject> userSettings = new ArrayList<PolarObject>();

	private Collection[] collection = new Collection[2];

	public Collection[] getCollection() {
		return collection;
	}

	public void setCollection(final Collection[] newCollections) {
		collection = newCollections;
	}

	public ArrayList<PolarObject> getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(final ArrayList<PolarObject> pos) {
		userSettings = pos;
	}

	/**
	 * @return the properties
	 */
	public ArrayList<Property> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final ArrayList<Property> properties) {
		this.properties = properties;
	}

}
