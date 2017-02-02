package com.cheers.basedata.info;

import java.util.Map;

public class Logs {
	/**
	 * 操作类型
	 * @param data
	 * @return 新闻、产品。。。。
	 */
	public static  String getLogInfo(Map data){
		String getTable=(String) data.get("table");
		String in="";
		if(getTable!=null){
		 if(getTable.equalsIgnoreCase("syscategory")){
				in="类型树";
				String categoryType=(String)(data.get("categoryType")==null?"":data.get("categoryType"));
				if(categoryType.equalsIgnoreCase("customerLevel")){
					in+="-客户级别";
				}else if(categoryType.equalsIgnoreCase("customerLimit")){
					in+="-客户权限";
				}else if(categoryType.equalsIgnoreCase("custType")){
					in+="-客户类型";
				}else if(categoryType.equalsIgnoreCase("feeSubject")){
					in+="-费用科目";
				}else if(categoryType.equalsIgnoreCase("feeType")){
					in+="-费用类型";
				}else if(categoryType.equalsIgnoreCase("gatherType")){
					in+="-收款方式";
				}else if(categoryType.equalsIgnoreCase("newsType")){
					in+="-新闻类型";
				}else if(categoryType.equalsIgnoreCase("orgType")){
					in+="-公司机构";
				}else if(categoryType.equalsIgnoreCase("payType")){
					in+="-付款方式";
				}else if(categoryType.equalsIgnoreCase("productLeibie")){
					in+="-产品分类";
				}else if(categoryType.equalsIgnoreCase("productType")){
					in+="-产品类型";
				}
			}else if(getTable.equalsIgnoreCase("product")){
				in="产品信息";
			}else if(getTable.equalsIgnoreCase("ProductPolicy")){
				in="产品价格政策";
			}else if(getTable.equalsIgnoreCase("ProductPolicyCDetail")){
				in="产品价格政策（设置）";
			}else if(getTable.equalsIgnoreCase("customer")){
				in="客户信息";
			}else if(getTable.equalsIgnoreCase("route")){
				in="拜访路线";
			}else if(getTable.equalsIgnoreCase("photoupload")){
				in="终端信息照片";
			}else if(getTable.equalsIgnoreCase("customerProduct")){
				in="客户库存";
			}else if(getTable.equalsIgnoreCase("TransferSlip")){
				in="调拨单";
			}else if(getTable.equalsIgnoreCase("store")){
				in="车辆/仓库";
			}else if(getTable.equalsIgnoreCase("StoreCheck")){
				in="盘点";
			}else if(getTable.equalsIgnoreCase("SaleStock")){
				in="出库单";
			}else if(getTable.equalsIgnoreCase("orders")){
				in="订单";
			}else if(getTable.equalsIgnoreCase("OrderStockRemove")){
				in="订单销售出库";
			}else if(getTable.equalsIgnoreCase("ReturnGoods")){
				in="退货单";
			}else if(getTable.equalsIgnoreCase("stockIo")){
				String IOTYPEID=String.valueOf(data.get("IOTYPEID"));
				if(IOTYPEID.equalsIgnoreCase("1")){
					in="产品入库信息";
				}else if(IOTYPEID.equalsIgnoreCase("2")){
					in="产品出库信息";
				}
			}else if(getTable.equalsIgnoreCase("BomMain")){
				in="Bom单";
			}else if(getTable.equalsIgnoreCase("BomPickMain")){
				in="领料信息";
			}else if(getTable.equalsIgnoreCase("BomProductInMain")){
				in="产成品信息";
			}else if(getTable.equalsIgnoreCase("Purchase")){
				in="费用申请";//---
			}else if(getTable.equalsIgnoreCase("PayIoDeatil")){
				String IOTYPEID=String.valueOf( data.get("iotype"));
				if(IOTYPEID.equalsIgnoreCase("1")){
					in="预收款";
				}else if(IOTYPEID.equalsIgnoreCase("2")){
					in="预付款";
				}
			}else if(getTable.equalsIgnoreCase("Paytype")){
				in="回款方式";
			}else if(getTable.equalsIgnoreCase("supplier")){
				in="供应商";
			}else if(getTable.equalsIgnoreCase("Purchase")){
				in="采购单";
			}else if(getTable.equalsIgnoreCase("WorkReport")){
				in="个人日志";
				//-----
			}else if(getTable.equalsIgnoreCase("Planorg")){
				in="公司/部门计划";
			}else if(getTable.equalsIgnoreCase("plantoperson")){
				in="人员计划";
			}else if(getTable.equalsIgnoreCase("news")){
				in="新闻";
			}else if(getTable.equalsIgnoreCase("sysaccount")){
				in="帐号";
				//-----
			} 
		}
		 return in;
	}
}
