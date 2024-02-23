package cn.bravedawn.dto;

import lombok.Data;

import java.util.List;

/**
 * 用来返回分页Grid的数据格式
 */
@Data
public class PagedGridResult<T> {
	
	private int page;			// 当前页数
	private int total;			// 总页数	
	private long records;		// 总记录数
	private List<T> rows;		// 每行显示的内容

}
