package com.cheers.commonMethod;

import java.util.Arrays;
import java.util.List;

public class SqlUtil {
	public static String getSqlStrByList(List sqhList, int splitNum, String columnName) {
		if (splitNum > 1000) // 因为数据库的列表sql限制，不能超过1000.
			return null;
		StringBuffer sql = new StringBuffer("");
		if (sqhList != null) {
			sql.append(" (").append(columnName).append(" IN (select * from dbo.split('");
			for (int i = 0; i < sqhList.size(); i++) {
				sql.append("").append(sqhList.get(i) + ",");
				if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
					sql.deleteCharAt(sql.length() - 1);
					sql.append("',',')) OR ").append(columnName).append(" IN (select * from dbo.split('");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append("',',')))");
		}
		return sql.toString();
	}

	/**
	 * 把超过1000的申请号数组拆分成数量splitNum的多组sql的in 集合。
	 * 
	 * @param sqhArrays
	 *            申请号的数组
	 * @param splitNum
	 *            拆分的间隔数目,例如： 1000
	 * @param columnName
	 *            SQL中引用的字段名例如： Z.SHENQINGH
	 * @return
	 */
	public static String getSqlStrByArrays(String[] sqhArrays, int splitNum,
			String columnName) {
		return getSqlStrByList(Arrays.asList(sqhArrays), splitNum, columnName);
	}
	public static String getSqlStrByArrays(String sqh, int splitNum,
			String columnName) {
		if(sqh!=null && !"".equals(sqh)) {
			return getSqlStrByList(Arrays.asList(sqh.split(",")), splitNum, columnName);
		}else {
			return "";
		}
		
	}
	public static String getSqlStrByArrays(String sqh, String columnName) {
		int splitNum = 800;
		if(sqh!=null && !"".equals(sqh)) {
			return getSqlStrByList(Arrays.asList(sqh.split(",")), splitNum, columnName);
		}else {
			return " and 1=2 ";
		}
//		return columnName +" in ("+sqh+")";
	}
	
	//例子  sql.append(" and "+SqlUtil.getSqlStrByArrays(params.get("accountIds").toString(),"om.CREATORID")+"");

}
