package com.gcitsolutions.libraryapp.Data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;


public abstract class BaseDAO<BaseEntity> implements ResultSetExtractor<List<BaseEntity>> {
	
	@Autowired
	public JdbcTemplate template;
	
	@Autowired
	public MongoTemplate mongoTemplate;
	
	private Integer pageSize;
	private Integer pageNo;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String frameLimitQuery(String sql, Integer pageNo, Integer pageSize){
		if(pageNo!=null){
			if(Integer.valueOf(pageNo)>0){
				int index=(pageNo-1)*pageSize;
				sql+=" LIMIT "+ index +","+pageSize;
			}
		}
		return sql;
	}
}
