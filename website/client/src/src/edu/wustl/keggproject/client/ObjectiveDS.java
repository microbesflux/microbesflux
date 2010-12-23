package edu.wustl.keggproject.client;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;



public class ObjectiveDS extends RestDataSource {
	private static ObjectiveDS instance = null;
	private static String baseurl = "http://128.252.160.238:8000/model/objective/";
	private ObjectiveDS(String id)
	{
			setID(id);		
			// Cross domain JSON
			setDataFormat(DSDataFormat.JSON);
		    setDataTransport(RPCTransport.SCRIPTINCLUDE);
		    setCallbackParam("callback");
		    
			DataSourceIntegerField pkField = new DataSourceIntegerField("pk");
			pkField.setHidden(true);
			pkField.setPrimaryKey(true);
			
			DataSourceField reactionid = new DataSourceField("r", FieldType.TEXT, "Reaction ID");
			DataSourceField weight = new DataSourceField("w", FieldType.TEXT, "Weight");
			setFields(pkField, reactionid, weight);
			
			setFetchDataURL(baseurl + "fetch/");
			setUpdateDataURL(baseurl + "update/");
		}
		
		public static ObjectiveDS getInstance(){
			if (instance == null) {
				instance = new ObjectiveDS("model");
				return instance;
			}
			else {
				return instance;
			}
		}
}	
