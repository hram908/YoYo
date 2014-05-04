/**   
* @Title: UtilChen.java
* @Package net.tsz.afinal
* @Description: TODO(用一句话描述该文件做什么)
* @author 陈红建
* @date 2013-9-13 下午4:51:35
* @version V1.0
*/ 
package com.asktun.mg.download;

import java.lang.reflect.Field;

import android.database.sqlite.SQLiteDatabase;

/** 
 * @ClassName: UtilChen
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈红建
 * @date 2013-9-13 下午4:51:35
 * 
 */
public class UtilChen
{

	public static void addTab(String defaultTableName , SQLiteDatabase db , Object obj) {
		String sql = "";
		try
		{
//			ContentValues values = new ContentValues();
			sql = "CREATE TABLE IF NOT EXISTS "+defaultTableName;
			StringBuffer sb = new StringBuffer();
			String l = "(";
			String r = ")";
			String d = ",";
			String varchar = "VARCHAR(200)"+d;
			//得到对象中的所有字段名称
			Field[] fields =  obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName()+" ";//得到要构造的字段名称
				sb.append(fieldName+varchar);
			}
			//得到构造好的 字段字符串
			//去掉结尾的","得到 "name VARCHAR(200),age VARCHAR(200)"
			sb.deleteCharAt(sb.lastIndexOf(","));
			sql = sql + l + sb.toString() + r; //得到构造好的sql语句
			//创建这个表
			db.execSQL(sql);
//			//将默认数据插入库中
//			db.insert(defaultTableName, null, values);
		}
		catch (SecurityException e)
		{
			
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			
			e.printStackTrace();
		}
		
	}
}

