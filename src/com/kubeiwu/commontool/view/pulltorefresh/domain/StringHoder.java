package com.kubeiwu.commontool.view.pulltorefresh.domain;

public class StringHoder {
	public String header_hint_normal,//
			header_hint_ready,//
			header_hint_loading, //
			footer_hint_ready, //
			footer_hint_normal;//
	public int footer_heaght, header_heaght = 60,arrow_pic;
	public StringHoder(String header_hint_normal, String header_hint_ready, String header_hint_loading, String footer_hint_ready,
			String footer_hint_normal, int footer_heaght, int header_heaght, int arrow_pic) {
		super();
		this.header_hint_normal = header_hint_normal;
		this.header_hint_ready = header_hint_ready;
		this.header_hint_loading = header_hint_loading;
		this.footer_hint_ready = footer_hint_ready;
		this.footer_hint_normal = footer_hint_normal;
		this.footer_heaght = footer_heaght;
		this.header_heaght = header_heaght;
		this.arrow_pic = arrow_pic;
	}
 

}