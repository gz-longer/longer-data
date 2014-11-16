package net.longersoft.data.linq.expressions;

import java.util.ArrayList;
import java.util.Calendar;

public enum ConditionOperator {
	Equal("@ = ?"),
	NotEqual("@ <> ?"),
	Like("@ like ?"),
	In("@ in (?)"),
	NotLike("@ not like ?"),
	NotIn("@ not in (?)"),
	GreaterEqual("@ >= ?"),
	GreaterThan("@ > ?"),
	LessEqual("@ <= ?"),
	LessThan("@ < ?"),
	IsNull("@ is null"),
	IsNullOrEmpty("@ is null or @ = ''"),
	Between("@ >= ? and @ <= ?"),
	
	IsToday("@ >= ? and @ < ?", true),
	IsTomorrow("@ >= ? and @ < ?", true),
	IsYesterday("@ >= ? and @ < ?", true),
	ThisYear("@ >= ? and @ < ?", true),
	ThisMonth("@ >= ? and @ < ?", true),
	ThisWeek("@ >= ? and @ < ?", true),
	
	Original("?"),
	
	None("");
	
	private String op;
	private Boolean isDateTime = false;
	private ConditionOperator(String op){
		this(op, false);
	}

	private ConditionOperator(String op, Boolean isDateTime){
		this.op = op;
		this.isDateTime = isDateTime;
	}
	
	public Boolean isDateTimeOperator(){
		return this.isDateTime;
	}

	@SuppressWarnings("deprecation")
	public ArrayList<Object> prepareValues(ArrayList<Object> values) {
		ArrayList<Object> list = values;
		
		switch(this){
		case Between:
			break;
			////
			case IsToday:{
				java.util.Date d = new java.util.Date();
				list = makeDateRange(d);
				break;
			}
			case IsTomorrow:{
				java.util.Date d = new java.util.Date();
				d.setDate(d.getDate() + 1);
				list = makeDateRange(d);
				break;
			}
			case IsYesterday:{
				java.util.Date d = new java.util.Date();
				d.setDate(d.getDate() - 1);
				list = makeDateRange(d);
				break;
			}
			default:
				
				break;
		}
		
		return list;
	}

	private ArrayList<Object> makeDateRange(java.util.Date d) {
		ArrayList<Object> list;
		list = new ArrayList<Object>();
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		java.sql.Date d1 = new java.sql.Date(d.getTime());
		d.setDate(d.getDate() + 1);
		java.sql.Date d2 = new java.sql.Date(d.getTime());
		list.add(d1);
		list.add(d2);
		return list;
	}
	
	public String value(){
		return this.op;
	}
	
	private int getParameterNumber(){
		int n = 0, i = -1;
		String s = this.op;
		i = s.indexOf("?");
		while( i >= 0){
			s = s.substring(i + 1);
			n++;
			i = s.indexOf("?");
		}
		return n;
	}
}
