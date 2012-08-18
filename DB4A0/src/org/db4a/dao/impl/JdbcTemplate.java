package org.db4a.dao.impl;




import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * �涨��
 *  1.���ݱ��� ��tb_�� + ����������
 *  2.���ݿ��ֶ�˳�����Ӧʵ������˳��һ�£����Ǳ���ģ�����ID�����ǵ�һ�����ԣ�
 * ���ܣ�ͨ������ʵ��CRUD
 * ʹ�ã��̳и���,���ö�Ӧ����,�����������
     DbUtil.Ϊ�Զ���������ݿ⹤����
 * @author Administrator
 *
 */
public class JdbcTemplate {

/* public final static String TABLE_PREFIX = "tb_";

 private String tableName = "";
 private Field[] fields;

 private List getFields(Object obj) {
  Class c = obj.getClass();
  // �������
  String className = obj.getClass().getSimpleName();
  // ���� + ǰ׺ = ����
  tableName = TABLE_PREFIX + className.toLowerCase();
  // ������е�����
  fields = c.getDeclaredFields();
  Method[] method = c.getMethods();
  List list = new ArrayList();
  for (int j = 0; j < fields.length; j++) {
   String m = "get" + fields[j].getName().toUpperCase().charAt(0)
     + fields[j].getName().substring(1);
   for (int i = 0; i < method.length; i++) {
    if (method[i].getName().endsWith(m)) {
     // method[i].invoke("", null);
     try {
      list.add(method[i].invoke(obj, null));
      System.out.println("---------"
        + method[i].invoke(obj, null));
     } catch (Exception e) {
      e.printStackTrace();
      return null;
     }
    }
   }
  }
  return list;
 }

 private String getInsertSQL(Object obj) {
  // ƴSQL���
  StringBuffer sql = new StringBuffer();
  sql.append("INSERT INTO ");
  sql.append("`" + tableName + "`");
  sql.append(" (");
  for (int i = 0; i < fields.length; i++) {
   sql.append("`" + fields[i].getName() + "`");
   if (i < fields.length - 1) {
    sql.append(",");
   }
  }
  sql.append(") ");
  sql.append(" VALUES(");
  for (int i = 0; i < fields.length; i++) {
   sql.append("?");
   if (i < fields.length - 1) {
    sql.append(",");
   }
  }
  sql.append(") ");
  return sql.toString();
 }

 private String getUpdateSQL(Object obj) {
  // ƴSQL���
  StringBuffer sql = new StringBuffer();
  sql.append("UPDATE ");
  sql.append("`" + tableName + "` ");
  sql.append(" SET ");
  for (int i = 1; i < fields.length; i++) {
   sql.append("`" + fields[i].getName() + "`");
   sql.append("=?");
   if (i < fields.length - 1) {
    sql.append(",");
   }
  }
  sql.append(" WHERE ");
  sql.append(fields[0].getName());
  sql.append("=?");
  return sql.toString();
 }

 private String getSelectSQL(Object obj) {
  // ƴSQL���
  StringBuffer sql = new StringBuffer();
  sql.append("SELECT * FROM ");
  sql.append("`" + tableName + "` ");
  return sql.toString();
 }

 private String getDeleteSQL(Object obj) {
  // ƴSQL���
  StringBuffer sql = new StringBuffer();
  sql.append("DELETE FROM ");
  sql.append("`" + tableName + "` ");
  sql.append(" WHERE ");
  sql.append(fields[0].getName());
  sql.append("=?");
  return sql.toString();
 }
 
 public boolean save(Object obj) throws Exception {
  // ��ȡobj�����Ե�ֵ
  List list = getFields(obj);
  // ��ȡsql
  String sql = getInsertSQL(obj);
  System.out.println(sql);
  // ͨ��DbUtil//�������ݿ�
  Connection conn = DbUtil.getConn();
  PreparedStatement ps = conn.prepareStatement(sql);
  
  for (int i = 1; i <= list.size(); i++) {
   ps.setObject(i, list.get(i-1));
  }
  boolean flag = ps.executeUpdate() > 0 ? true : false;
  DbUtil.close(conn);
  return flag;
 }

 public boolean update(Object obj) throws Exception {
  // ��ȡobj�����Ե�ֵ
  List list = getFields(obj);
  // ��ȡsql
  String sql = getUpdateSQL(obj);
  // ͨ��DbUtil
  Connection conn = DbUtil.getConn();
  PreparedStatement ps = conn.prepareStatement(sql);

  for (int i = 1; i < list.size(); i++) {
   ps.setObject(i, list.get(i));
  }
  ps.setInt(list.size(), (Integer) list.get(0));
  boolean flag = ps.executeUpdate() > 0 ? true : false;
  DbUtil.close(conn);
  return flag;
 }

 public List select(Object obj) throws Exception {
  return select(obj,null,null);
 }

 public List select(Object obj,String fieldName,Object fieldValue) throws Exception {
  // ��ȡobj�����Ե�ֵ
  List lists = getFields(obj);
  // ��ȡsql
  String sql = getSelectSQL(obj);
  if(fieldName != null)
   sql += " WHERE `"+ fieldName +"` =?";
  // ͨ��DbUtil
  System.out.println("SQL:"+sql);
  Connection conn = DbUtil.getConn();
  PreparedStatement ps = conn.prepareStatement(sql);
  if(fieldName != null)
   ps.setObject(1, fieldValue);
  ResultSet rs = ps.executeQuery();
  Class c = obj.getClass();
  Method[] method = c.getMethods();
  List list = new ArrayList();
  while (rs.next()) {
   Object o = c.newInstance();
   // ������е�����
   for (int j = 0; j < fields.length; j++) {
    String m = "set" + fields[j].getName().toUpperCase().charAt(0)
      + fields[j].getName().substring(1);
    for (int i = 0; i < method.length; i++) {
     if (method[i].getName().endsWith(m)) {
      //System.out.println("dd"+rs.getObject(j));
      try{
       method[i].invoke(o, rs.getObject(j+1));
      } catch (Exception e) {
       e.printStackTrace();
      }
     }
    }
   }
   list.add(o);
  }
  DbUtil.close(conn);
  return list;
 }
 
 public boolean delete(Object obj,Integer id) throws Exception {
  // ��ȡobj�����Ե�ֵ
  List list = getFields(obj);
  // ��ȡsql
  String sql = getDeleteSQL(obj);
  // ͨ��DbUtil
  Connection conn = DbUtil.getConn();
  PreparedStatement ps = conn.prepareStatement(sql);
  ps.setInt(1, id);
  boolean flag = ps.executeUpdate() > 0 ? true : false;
  DbUtil.close(conn);
  return flag;
 }*/

}

