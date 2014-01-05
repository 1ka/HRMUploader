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
import java.util.Hashtable;

import com.softwyer.hrmuploader.polar.Status;
import com.softwyer.hrmuploader.polar.UserPreferences;

public class Webservice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2535701712546100748L;

	public static final String KEY_STATUS = "status";
	public static final String KEY_USER_PREFERENCES = "userPreferences";

	private Hashtable<String, Property[]> objects = new Hashtable<String, Property[]>();

	private final ArrayList<Collection> collection = new ArrayList<Collection>();

	/**
	 * @return the objects
	 */
	public Hashtable<String, Property[]> getObject() {
		return objects;
	}

	/**
	 * @param objects
	 *            the objects to set
	 */
	public void setObject(final Hashtable<String, Property[]> objects) {
		this.objects = objects;
	}

	/**
	 * Search this instance of the returned Webservice object and pull out the
	 * status, if any.
	 * 
	 * @return
	 */
	public Status getStatus() {
		final Property[] props = getObject().get(KEY_STATUS);

		return Status.unmarshal(props);
	}

	public UserPreferences getUserPreferences() {

		final Property[] props = getObject().get(KEY_USER_PREFERENCES);

		return UserPreferences.unmarshal(props);
	}

	public ArrayList<Collection> getCollection() {
		return collection;
	}

}
