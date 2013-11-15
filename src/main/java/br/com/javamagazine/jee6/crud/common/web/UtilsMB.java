package br.com.javamagazine.jee6.crud.common.web;

import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class UtilsMB {
	
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

}
