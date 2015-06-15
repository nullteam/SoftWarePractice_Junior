package com.universer.HustWhereToEat.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.universer.HustWhereToEat.model.Order;
import com.universer.HustWhereToEat.model.Restaurant;

public class HWDataBaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "hwsqlite.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<Restaurant, String> restaurantDao;
	private Dao<Order, String> orderDao;

	public HWDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, Restaurant.class);
			TableUtils.createTable(connectionSource, Order.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(connectionSource, Restaurant.class, true);
			TableUtils.dropTable(connectionSource, Order.class, true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Dao<Restaurant, String> getRestaurantDao() throws SQLException {
		if (null == restaurantDao) {
			restaurantDao = getDao(Restaurant.class);
		}
		return restaurantDao;
	}

	public Dao<Order, String> getOrderDao() throws SQLException {
		if (null == orderDao) {
			orderDao = getDao(Order.class);
		}
		return orderDao;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		orderDao = null;
		restaurantDao = null;
	}

}
