package org.db4a.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
	
	public abstract long insert(T entity);
	
	public abstract void delete(int id);
	
	public abstract void delete(Integer... ids);

	public abstract void update(T entity);

	public abstract T get(int id);

	public abstract List<T> rawQuery(String sql, String[] selectionArgs);

	public abstract List<T> find();

	public abstract List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit);

	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs);
	
	public void execSql(String sql, Object[] selectionArgs);

}