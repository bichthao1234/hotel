package com.my.hotel.script;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GenerateSqlScriptTests {

	private static final String MSSQL = "mssql";
	private static final String MYSQL = "mysql";

	@Autowired
	private GenerateSqlScript generateSqlScript;
	
	@Test
	void generateSqlScriptMssql() throws Exception {
		generateSqlScript.generateScript(MSSQL);
	}

	@Test
	void generateSqlScriptMysql() throws Exception {
		generateSqlScript.generateScript(MYSQL);
	}
}
