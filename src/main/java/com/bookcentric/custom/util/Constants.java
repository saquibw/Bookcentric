package com.bookcentric.custom.util;

public class Constants {
	
	public static String STATUS_ACTIVE = "Active";
	public static String STATUS_CANCEl = "Cancelled";
	public static String STATUS_PENDING = "Pending";
	public static String STATUS_BREAK = "Break";
	public static String STATUS_DELETED = "Deleted";
	
	public static String TYPE_BEST_SELLER = "bestSeller";
	public static String TYPE_NEW_ARRIVAL = "newArrival";
	public static String TYPE_CHILDREN = "children";
	public static String TYPE_READING_CHALLENGE = "readingChallenge";
	
	public static String ACTION_ADD = "add";
	public static String ACTION_REMOVE = "remove";
	
	public static Integer BOOKS_COUNT_PER_PAGE = 30;
	
	public static Integer EXPIRY_AFTER_DAYS = 2;
	
	public static String ROLE_ADMIN = "admin";
	public static String ROLE_USER = "user";
	
	public static String URL_BASE = "https://bookcentricbd.com";
	public static String URL_LOGIN = URL_BASE + "/login";
	public static String URL_LOGO = URL_BASE + "/images/bc-logo-small.png";
	public static String URL_MANAGEMENT =  URL_BASE + "/management";
	public static String URL_MANAGEMENT_USER = URL_MANAGEMENT + "/user";
	public static String URL_TREASURY = URL_BASE + "/treasury";
	public static String URL_BLOGS = URL_BASE + "/blogs/all/view";
	public static String URL_EVENTS = URL_BASE + "/events/all";
	public static String URL_READING_CHALLENGES = URL_BASE + "/reading-challenge/all";
	public static String URL_GIFT_SUBSCRIPTION = URL_BASE + "/gift-subscription";
	public static String URL_DONATE_BOOKS = URL_BASE + "/donate-books";
	public static String URL_WISH_FOR_A_BOOK = URL_BASE + "/wishlist";
	
	public static String FILE_NAME_THINGS_TO_REMEMBER = "Things_To_Remember.pdf";
	public static String URL_THINGS_TO_REMEMBER = "/static/docs/" + FILE_NAME_THINGS_TO_REMEMBER;

}
