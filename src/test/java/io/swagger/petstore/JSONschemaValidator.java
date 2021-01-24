package io.swagger.petstore;

import static io.restassured.RestAssured.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.swagger.utils.ExcelUtils;


public class JSONschemaValidator {
	
	@Test (priority = 1)
	public void testSchemaGet() {
		
		baseURI = "https://petstore.swagger.io/v2";
		
		given().
			get("/pet/202010").
		then().
			assertThat().
			body(matchesJsonSchemaInClasspath("schema.json")).
			statusCode(HttpStatus.SC_OK);
			
	}
	
	//@Test (priority = 0)
	public void testSchemaPost() throws IOException {
		baseURI = "https://petstore.swagger.io/v2";
		
		ExcelUtils dataPetsExcel = ExcelUtils.lerArquivoExcelUsers();

		JSONObject mypet = new JSONObject();
		
		for (int i = 1; i < dataPetsExcel.getRowCount(); i++) {
			mypet.put("id", dataPetsExcel.getCellData(i, 0));

			JSONObject itens_catg = new JSONObject();

			itens_catg.put("id", dataPetsExcel.getCellData(i, 1));
			itens_catg.put("name", dataPetsExcel.getCellData(i, 2));
			
			mypet.put("category", itens_catg);
			mypet.put("name", dataPetsExcel.getCellData(i, 3));
			
			JSONArray photo = new JSONArray();
			photo.add(dataPetsExcel.getCellData(i, 4));

			mypet.put("photoUrls", photo);

			JSONObject itens_tag = new JSONObject();
			itens_tag.put("id", dataPetsExcel.getCellData(i, 5));
			itens_tag.put("name", dataPetsExcel.getCellData(i, 6));

			JSONArray tag = new JSONArray();
			tag.add(itens_tag);

			mypet.put("tags", tag);
			
			mypet.put("status", dataPetsExcel.getCellData(i, 7));
		
		given().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(mypet.toJSONString()).
		when().
			post("/pet").
		then().
			assertThat().
			body(matchesJsonSchemaInClasspath("schema_post.json")).
			statusCode(HttpStatus.SC_OK);
		}
		
	}

}
