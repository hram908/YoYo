/**   
* @Title: UtilChen.java
* @Package net.tsz.afinal
* @Description: TODO(��һ�仰�������ļ���ʲô)
* @author �º콨
* @date 2013-9-13 ����4:51:35
* @version V1.0
*/ 
package com.asktun.mg.download;

import java.lang.reflect.Field;

import android.database.sqlite.SQLiteDatabase;

/** 
 * @ClassName: UtilChen
 * @Description: TODO(������һ�仰��������������)
 * @author �º콨
 * @date 2013-9-13 ����4:51:35
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
			//�õ������е������ֶ�����
			Field[] fields =  obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName()+" ";//�õ�Ҫ������ֶ�����
				sb.append(fieldName+varchar);
			}
			//�õ�����õ� �ֶ��ַ���
			//ȥ����β��","�õ� "name VARCHAR(200),age VARCHAR(200)"
			sb.deleteCharAt(sb.lastIndexOf(","));
			sql = sql + l + sb.toString() + r; //�õ�����õ�sql���
			//���������
			db.execSQL(sql);
//			//��Ĭ�����ݲ������
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

