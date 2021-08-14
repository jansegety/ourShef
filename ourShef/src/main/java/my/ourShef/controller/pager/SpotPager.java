package my.ourShef.controller.pager;

import lombok.Data;

@Data
public class SpotPager {

	//leftPage
	private Long leftPage;
	//rightPage
	private Long rightPage;
	
	//startPage
	private Long startPage;
	//endPage
	private Long endPage;
	
	//current Page
	private Long currentPage;
	//total PageNum
	private Long totalPageNum;
	//current PageGroup
	private Long currentPageGroup;
	//total PageGroupNum
	private Long totalPageGroupNum;
	
	
	//default 10
	private Long tupleNumByPage;
	//default 5
	private Long pageNumByGroup;
	//total tuple Num
	private Long totalTupleNum;
	
	
	/*
	 * default
	 * tupleNumByPage : 10
	 * pageNumByGroup: 5
	 */
	public SpotPager(Long currentPage, Long totalTupleNum) {
	this(10L, 5L, currentPage, totalTupleNum);
	}
	
	public SpotPager(Long tupleNumByPage, Long pageNumByGroup, Long currentPage, Long totalTupleNum) {
		this.tupleNumByPage = tupleNumByPage;
		this.pageNumByGroup = pageNumByGroup;
		this.currentPage = currentPage;
		this.totalTupleNum = totalTupleNum;
		
		if((totalTupleNum%tupleNumByPage)==0)
		{
			totalPageNum = totalTupleNum/tupleNumByPage;
		}
		else
		{
			totalPageNum = (totalTupleNum/tupleNumByPage) + 1;
		}
		if(totalPageNum == 0)
			totalPageNum=1L;
		
		
		
		leftPage = currentPage-1;
		if(leftPage < 1)
		{
			leftPage = 1L;
		}
		
		rightPage = currentPage + 1;
		if(rightPage > totalPageNum)
		{
			rightPage = totalPageNum;
		}
		
		if(currentPage%pageNumByGroup==0)
		{
			currentPageGroup = currentPage/pageNumByGroup;
		}
		else
		{
			currentPageGroup = (currentPage/pageNumByGroup) + 1;
		}
		
		if(totalPageNum%pageNumByGroup==0)
		{
			totalPageGroupNum = totalPageNum/pageNumByGroup;
		}
		else
		{
			totalPageGroupNum = (totalPageNum/pageNumByGroup)+1;
		}
		
		
		startPage = ((currentPageGroup-1) * pageNumByGroup) + 1;
		
		
		//if currentPageGroup is last
		if(currentPageGroup == totalPageGroupNum) {
			//endPage is lastPage
			endPage = totalPageNum;
		}
		else {
			if(currentPageGroup * pageNumByGroup < totalPageNum)
			endPage = currentPageGroup * pageNumByGroup;
			else
			endPage = totalPageNum;	
		}
		
	}
			
}
