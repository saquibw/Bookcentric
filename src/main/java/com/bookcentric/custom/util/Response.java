package com.bookcentric.custom.util;

import java.util.Map;

import lombok.Data;

@Data
public class Response{
	public boolean success;
	public Object data;
	public Map<String, Object> dataMap;
}
