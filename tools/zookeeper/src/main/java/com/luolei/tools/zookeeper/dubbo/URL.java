package com.luolei.tools.zookeeper.dubbo;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class URL implements Serializable {

	private static final long serialVersionUID = 1009258150241657900L;

    private final String username;

    private final String password;

    private final String connectString;

    private final String path;

    private final Map<String, String> parameters;
    
    protected URL() {
        this.username = null;
        this.password = null;
        this.connectString = null;
        this.path = null;
        this.parameters = null;
    }
    
    public URL(String connectString, String path) {
    	this(null, null, connectString, path, null);
    }
    
    public URL(String connectString, String path, Map<String, String> parameters) {
    	this(null, null, connectString, path, parameters);
    }
    
    public URL(String username, String password, String connectString, String path, Map<String, String> parameters) {
    	this.username = username;
        this.password = password;
        this.connectString = connectString;
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        } else {
            parameters = new HashMap<String, String>(parameters);
        }
        this.parameters = Collections.unmodifiableMap(parameters);
    }
    
    public static String encode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

    public String getConnectString() {
		return connectString;
	}

	public String getAuthority() {
        if ((username == null || username.length() == 0)
                && (password == null || password.length() == 0)) {
            return null;
        }
        return (username == null ? "" : username)
                + ":" + (password == null ? "" : password);
    }


	public String getPath() {
		return path;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
    
}
