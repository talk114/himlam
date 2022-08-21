package vn.nganluong.naba.service.impl;

import org.springframework.stereotype.Service;

import vn.nganluong.naba.service.CronService;

@Service
public class CrontServiceImpl implements CronService {

	@Override
	public String getCront() {
//		return "* * * * * *";
		return "0 * 0 ? * * ";
		
		// https://www.baeldung.com/cron-expressions
		// 0 0 12 * * ? 2017;
	}


}