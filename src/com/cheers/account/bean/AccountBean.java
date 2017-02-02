/**
 * 
 */
package com.cheers.account.bean;

import java.sql.ResultSet;
import java.util.List;

import com.cheers.database.DataBase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author apple
 * @version 1.1
 */
@JsonIgnoreProperties(ignoreUnknown=true) 
public class AccountBean {
	
	
	protected String id;
	protected String account;//账号
	protected String name;//名字
	protected String position;//职位
	protected String age;//年龄
	protected String address;//地址
	protected String mobile;//手机
	protected String tel;//电话
	protected String email;//电子邮箱
	protected String sex;//性别
	protected String password;//密码
	protected String birthday;//生日
	protected String createTime;//
	protected String enable;//是否启用 1是0否
	protected String idCard;//身份证
	protected String salaryAccount;//工资账号
	protected String car;//是否有车
	protected String carInfo;//车辆信息
	protected String wageStandard;//工资标准
	protected String costStandard;//费用标准
	protected String workType;//工作类型 
	protected String personTypeId;//员工分类
	protected String entryTime;//入职时间
	protected String memo;//备注
	protected String worksn;//工作号
   protected String roleid;
   protected String createrid;

	protected String accountType;//账号类型 1公司2门店
	private String _canAccountIds;  //此账号所能看到的账号ids 下级
	private String _upAccountIds;  //此账号上级的账号ids 上级
	private String _canClientIds;  //此账号所能看到的客户ids 自己设置的加上下级业务员所包含的

	
	protected String _tableName = "account";
	
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the nick
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nick
	 *            the nick to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSalaryAccount() {
		return salaryAccount;
	}

	public void setSalaryAccount(String salaryAccount) {
		this.salaryAccount = salaryAccount;
	}

	public String getWorksn() {
		return worksn;
	}

	public void setWorksn(String worksn) {
		this.worksn = worksn;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(String carInfo) {
		this.carInfo = carInfo;
	}

	public String getWageStandard() {
		return wageStandard;
	}

	public void setWageStandard(String wageStandard) {
		this.wageStandard = wageStandard;
	}

	public String getCostStandard() {
		return costStandard;
	}

	public void setCostStandard(String costStandard) {
		this.costStandard = costStandard;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getPersonTypeId() {
		return personTypeId;
	}

	public void setPersonTypeId(String personTypeId) {
		this.personTypeId = personTypeId;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	

	public String get_tableName() {
		return _tableName;
	}

	public void set_tableName(String _tableName) {
		this._tableName = _tableName;
	}

	// 登录检查函数
	// 返回值:0_正确,1_无此操作员 2_操作员代码已无效 3_操作员密码不正确
	public static AccountBean  getById(String id) {
		DataBase db = null;
		AccountBean act=null;
		int flag=0;
		try {
			db = new DataBase();
			db.connectDb();
			act =AccountBean.getById(db, id);
			flag=1;
				
		} catch (Exception e) {
			e.printStackTrace();
			flag=0;
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if(flag==1)
			return act;
		else 
			return null;
		

	}

	public static AccountBean  getById(DataBase db,String id) {
		
		AccountBean act=new AccountBean();
		int flag=0;
		try {
			ResultSet rs=null;
			String sqltext = "SELECT a.* "
					+ " FROM account a"
					+ " WHERE a.id="
					+ id+" and enable=1  ";
			//System.out.println(sqltext);
			rs = db.queryAll(sqltext);
			if (rs.next()) { // 如果有操作员
				//act.id=rs.getString("accountid");
				act.id	 = rs.getString("id");
				act.account =rs.getString("account");
				act.name=rs.getString("name");
				act.position=rs.getString("position");
				act.memo=rs.getString("memo");
				act.age=rs.getString("age");
				act.address=rs.getString("address");
				act.mobile=rs.getString("mobile");
				act.email=rs.getString("email");
				act.workType=rs.getString("workType");
				act.tel=rs.getString("tel");
				act.worksn=rs.getString("worksn");
				act.enable=rs.getString("enable");
				act.roleid=rs.getString("roleid");
				flag=1;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag=0;
		} finally {
			try {
				db.closer();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if(flag==1)
			return act;
		else 
			return null;
		

	}
   public String getRoleid() {
     return this.roleid;
   }
   public void setRoleid(String roleid) {
     this.roleid = roleid;
   }
   public String getCreaterid() {
     return this.createrid;
   }
   public void setCreaterid(String createrid) {
     this.createrid = createrid;
   }
	public String get_canAccountIds() {
		return _canAccountIds;
	}

	public void set_canAccountIds(String _canAccountIds) {
		this._canAccountIds = _canAccountIds;
	}

	public String get_upAccountIds() {
		return _upAccountIds;
	}

	public void set_upAccountIds(String _upAccountIds) {
		this._upAccountIds = _upAccountIds;
	}

	public String get_canClientIds() {
		return _canClientIds;
	}

	public void set_canClientIds(String _canClientIds) {
		this._canClientIds = _canClientIds;
	}
	
}
