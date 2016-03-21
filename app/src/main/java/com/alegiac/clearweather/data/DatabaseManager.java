package com.alegiac.clearweather.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alegiac.clearweather.model.City;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper   {

	private static final String DATABASE_NAME = "db_city.sqlite";
	private static final String TABLE_NAME = "city_list";
	
	// Contacts table name
	//private static final String TABLE_DATA = "table_data";
	
	private static final int DATABASE_VERSION = 1;
	
	//Table Columns names
	/*
	private static final String KEY_ID 		= "id";
	private static final String KEY_NAME 	= "name";	
	private static final String KEY_CODE 	= "code";	
	private static final String KEY_WEATHER = "jsonWeather";
	private static final String KEY_FORECAST = "jsonForecast";
	*/
	
	public static SQLiteDatabase db;
	private DataBaseHelper dbHelper;
	public static Context context;
	//public GlobalVariable global;
	
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		//Activity act = (Activity) context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME);
		db = dbHelper.openDataBase();
		//createTable(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close() {
		db.close();
	}
	
	/*
	private void createTable(SQLiteDatabase db){
		String CREATE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA + "("
				+ KEY_ID + " TEXT PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"	
				+ KEY_CODE + " TEXT,"		
				+ KEY_WEATHER + " TEXT,"
				+ KEY_FORECAST + " TEXT"
				+ ")";
		db.execSQL(CREATE_DATA_TABLE);
	}
	
	public ItemLocation getLocation(String id) {
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_DATA, 
				new String[] { 
				KEY_ID,
				KEY_NAME,
				KEY_CODE,
				KEY_WEATHER,
				KEY_FORECAST }, 
				KEY_ID + "=?",
				new String[] { id }, null, null, null, null);
		ItemLocation itemloc = new ItemLocation("null", "null", "null", "null", "null");
		if (cursor != null && isLocationExist(id)){
			cursor.moveToFirst();
			itemloc.setId(cursor.getString(0));
			itemloc.setName(cursor.getString(1));
			itemloc.setCode(cursor.getString(2));
			itemloc.setJsonWeather(cursor.getString(3));
			itemloc.setJsonForecast(cursor.getString(4));
		}
		return itemloc;
	}
	
	public void saveLocation(ItemLocation itemloc){
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, itemloc.getId());
		values.put(KEY_NAME, itemloc.getName());
		values.put(KEY_CODE, itemloc.getCode());
		values.put(KEY_WEATHER, itemloc.getJsonWeather());
		values.put(KEY_FORECAST, itemloc.getJsonForecast());
		// insert or update
		if(isLocationExist(itemloc.getId())){
			// Inserting Row
			db.update(TABLE_DATA, values, KEY_ID + " = ?", new String[] { itemloc.getId() });
			Toast.makeText(context, "Update", Toast.LENGTH_LONG).show();
		}else{
			// Inserting Row
			db.insert(TABLE_DATA, null, values);
			Toast.makeText(context, "Insert", Toast.LENGTH_LONG).show();
		}
		db.close(); // Closing database connection
	}
	
	public boolean isLocationExist(String id) {
		String countQuery = "SELECT  * FROM " + TABLE_DATA +" WHERE "+KEY_ID+"='"+id+"'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count=cursor.getCount();
		cursor.close();
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	// Deleting single row
	public void deleteLocation(String id) {
		db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID + " = ?", new String[] { id });
		db.close();
		if(id.equals(global.getStringPref(global.S_KEY_CURRENT_ID, global.getDefaultCity()))){
			if(getListCode().size()>=0){
				global.setStringPref(global.S_KEY_CURRENT_ID, getListCode().get(0));
			}else{
				global.setStringPref(global.S_KEY_CURRENT_ID, "null");
			}
		}
	}
	
	public ArrayList<String> getListCode(){
		ArrayList<String> listcode = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT "+KEY_CODE+" FROM " + TABLE_DATA;

		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				listcode.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		// return contact list
		return listcode;
	}
	*/
	
	
	public ArrayList<City> getCity() {
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME;
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				City city = new City();
				city.setId(c.getString(0));
				city.setName(c.getString(1));
				city.setLat(c.getString(2));
				city.setLng(c.getString(3));
				city.setCode(c.getString(4));
				data.add(city);
			} while (c.moveToNext());
		}
		db.close();
		return data;

	}
	public ArrayList<City> getWords(String string) {
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE name LIKE '"+string+"%' OR name LIKE '"+string+"' LIMIT 5";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				City city = new City();
				city.setId(c.getString(0));
				city.setName(c.getString(1));
				city.setLat(c.getString(2));
				city.setLng(c.getString(3));
				city.setCode(c.getString(4));
				data.add(city);
			} while (c.moveToNext());
		}
		db.close();
		return data;
	}
	public City getWordsFormAutocomplate(String s) {
		City city = null;
		String scity = "", scode="";
		if(s.contains(",")){
			scity=s.split("\\,")[0];
			scode=s.split("\\,")[1].trim();
		}else{
			return null;
		}
		
		ArrayList<City> data = new ArrayList<City>();
		db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE name= '"+scity+"' AND code= '"+scode+"'";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			city = new City();
			city.setId(c.getString(0));
			city.setName(c.getString(1));
			city.setLat(c.getString(2));
			city.setLng(c.getString(3));
			city.setCode(c.getString(4));
		}
		
		db.close();
		return city;
	}
}
