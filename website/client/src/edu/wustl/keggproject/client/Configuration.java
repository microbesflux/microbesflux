/* 
 * Copyright (c) 2011, Eric Xu
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, 
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation 
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *  POSSIBILITY OF SUCH DAMAGE.
 */

package edu.wustl.keggproject.client;

public class Configuration {
	// private String baseurl = "http://128.252.160.238:8000/";

	// private String baseurl = "http://127.0.0.1:8000/";
	private String baseurl = "http://tanglab.engineering.wustl.edu/flux/";
	private String email = "";
	private String uploadFile="";
	
	
	private boolean login = false;
	
	
	public void setLogin(boolean l) {
		login = l;
	}
	
	public boolean getLogin() {
		return login;
	}
	
	public String getBaseURL() {
		return baseurl;
	}

	public void setBaseURL(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getCurrentCollection() {
		return currentCollection;
	}
	
	public String getCurrentEmail() {
		return email;
	}
	
	public void setCurrentEmail(String e) {
		email = e;
	}

	public String getUploadFile() {
		return uploadFile;
	}
	
	public void setUploadFile(String f) {
		uploadFile = f;
	}
	public void setCurrentCollection(String currentCollection) {
		this.currentCollection = currentCollection;
	}

	private String currentCollection = "";

	public Configuration(String secret) {
		if (!secret.equals("secret")) {
			throw new RuntimeException(
					"Not suppose to call the constructor that way");
		}
	}
	
	
}
