package com.ibm.wcts;

public class Page {
	
//  The macros expect you to add a model attribute called �paginationData� 
//	which contains (at least) the following properties:
	
//	pageNumber -> The current page number
//	pageSize -> The number of items in each page
//	pagesAvailable -> The total number of pages
//	sortDirection -> The sorting direction (ascending or descending)
//	sortField -> The field currently sorted by


	public int pageNumber;
	public int pageSize;
	public int pagesAvailable;
	public String sortDirection;
	public String sortField;
	
	public Page(){}
	
	
	
}
