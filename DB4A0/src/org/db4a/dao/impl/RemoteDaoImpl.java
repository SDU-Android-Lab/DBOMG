package org.db4a.dao.impl;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.db4a.annotation.Column;
import org.db4a.annotation.Id;
import org.db4a.annotation.Table;
import org.db4a.dao.BaseDao;
import org.db4a.util.RemoteDBHelper;
import org.db4a.util.TableHelper;

import android.util.Log;

public class RemoteDaoImpl<T> implements BaseDao<T> {
	private String tableName;
	private String idColumn;
	private Class<T> clazz;
	private List<Field> allFields;
	RemoteDBHelper rdb;

	@SuppressWarnings("unchecked")
	public RemoteDaoImpl(RemoteDBHelper rdb) {
		this.rdb = rdb;
		this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType) super
				.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

		if (this.clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) this.clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}
		this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(),
				this.clazz.getSuperclass().getDeclaredFields());
		for (Field field : this.allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				this.idColumn = column.name();
				break;
			}
		}
		Log.d("db4a", "clazz:" + this.clazz + " tableName:" + this.tableName
				+ " idColumn:" + this.idColumn);
	}

	public long insert(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	public void delete(Integer... ids) {
		// TODO Auto-generated method stub

	}

	public void update(T entity) {
		// TODO Auto-generated method stub

	}

	public T get(int id) {
		String selection = this.idColumn + " = ?";
		String[] selectionArgs = { Integer.toString(id) };
		List<T> list = find(null, selection, selectionArgs, null, null, null,
				null);
		if ((list != null) && (list.size() > 0)) {
			return (T) list.get(0);
		}
		return null;
	}

	public List<T> rawQuery(String sql, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> find() {
		return find(null, null, null, null, null, null, null);
	}

	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	public void execSql(String sql, Object[] selectionArgs) {

	}

	@Override
	public List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		String sql = getSelectSQL();
		sql=sql.replace("*", getColumnsSQL(columns));
		sql+=" WHERE "+getSelectionArgs(selection, selectionArgs);
		sql+=getPariBySQL("GROUP BY", groupBy);
		sql+=getPariBySQL("HAVEING", having);
		sql+=getPariBySQL("ORDER BY", orderBy);
		sql+=getPariBySQL("", limit);
		if(sql.endsWith(" WHERE ")){
			sql=sql.substring(0, sql.lastIndexOf(" WHERE"))+";";
		}
		Log.i("db4a", sql);
		List<T> list = new ArrayList<T>();
		Connection con = null;
		try {
			con = this.rdb.getConnection();
			 Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			getListFromResultSet(list, rst);
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			Log.e("db4a", "[find] from DB Exception");
			e.printStackTrace();
		}
		return list;
	}

	private void getListFromResultSet(List<T> list, ResultSet rs)
			throws IllegalAccessException, InstantiationException,
			IllegalArgumentException, SecurityException, SQLException {
		while (rs.next()) {
			T entity = this.clazz.newInstance();

			for (Field field : this.allFields) {
				Column column = null;
				if (field.isAnnotationPresent(Column.class)) {
					column = (Column) field.getAnnotation(Column.class);

					field.setAccessible(true);
					Class<?> fieldType = field.getType();
					int c =-1;
					try{
						c=rs.findColumn(column.name());
					} catch (SQLException e) {
						continue;
					}finally{
						
					}if (c < 0) {
						continue; // 如果不存则循环下个属性值
					} else if ((Integer.TYPE == fieldType)
							|| (Integer.class == fieldType)) {
						field.set(entity, rs.getInt(c));
					} else if (String.class == fieldType) {
						field.set(entity, rs.getString(c));
					} else if ((Long.TYPE == fieldType)
							|| (Long.class == fieldType)) {
						field.set(entity, Long.valueOf(rs.getLong(c)));
					} else if ((Float.TYPE == fieldType)
							|| (Float.class == fieldType)) {
						field.set(entity, Float.valueOf(rs.getFloat(c)));
					} else if ((Short.TYPE == fieldType)
							|| (Short.class == fieldType)) {
						field.set(entity, Short.valueOf(rs.getShort(c)));
					} else if ((Double.TYPE == fieldType)
							|| (Double.class == fieldType)) {
						field.set(entity, Double.valueOf(rs.getDouble(c)));
					} else if (Blob.class == fieldType) {
						field.set(entity, rs.getBlob(c));
					} else if (byte[].class == fieldType) {
						field.set(entity, rs.getBlob(c));
					} else if (Character.TYPE == fieldType) {
						String fieldValue = rs.getString(c);

						if ((fieldValue != null) && (fieldValue.length() > 0)) {
							field.set(entity,
									Character.valueOf(fieldValue.charAt(0)));
						}
					}
				}
			}

			list.add((T) entity);
		}
	}
	
	private String getSelectSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append("" + tableName + " ");
		return sql.toString();
	}

	private String getColumnsSQL(String columns[]) {
		if (columns == null || columns.length == 0) {
			return "*";
		}
		StringBuffer sql = new StringBuffer();
		for (String c : columns) {
			sql.append(c + ",");
		}
		return sql.substring(0, sql.length()-1);
	}
	
	private String getSelectionArgs(String selection,String[] selectionArgs){
		if(selection==null||selection.length()==0){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		if(selectionArgs==null||selectionArgs.length==0){
			return selection;
		}
		sb.append(selection);
		int index=0;
		while(sb.indexOf("?")>0&&index<selectionArgs.length){
			sb.replace(sb.indexOf("?"), sb.indexOf("?")+1, "'"+selectionArgs[0]+"'");
			index++;
		}
		sb.append(" ");
		return sb.toString().replace("?", "*");
	}
	
	private String getPariBySQL(String SQL,String value){
		if(value==null||value.length()==0){
			return "";
		}else {
			return SQL+" "+value+" ";
		}
	}
	
	private String getCommonSQL(String sql,Object[] selectionArgs){
		if(sql==null||sql.length()==0){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		if(selectionArgs==null||selectionArgs.length==0){
			return sql;
		}
		sb.append(sql);
		int index=0;
		while(sb.indexOf("?")>0&&index<selectionArgs.length){
			sb.replace(sb.indexOf("?"), sb.indexOf("?")+1, "'"+selectionArgs[0]+"'");
			index++;
		}
		sb.append(" ");
		return sb.toString().replace("?", "*");
	}

}
