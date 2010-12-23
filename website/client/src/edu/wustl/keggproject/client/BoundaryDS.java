package edu.wustl.keggproject.client;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;



public class BoundaryDS extends RestDataSource {
	private static BoundaryDS instance = null;
	private static String baseurl = "http://128.252.160.238:8000/model/bound/";
	private BoundaryDS(String id)
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
			DataSourceField lb = new DataSourceField("l", FieldType.TEXT, "lb");
			DataSourceField ub = new DataSourceField("u", FieldType.TEXT, "ub");
			setFields(pkField, reactionid, lb,ub);
						
			setFetchDataURL(baseurl + "fetch/");
			setUpdateDataURL(baseurl + "update/");
		}
		
		public static BoundaryDS getInstance(){
			if (instance == null) {
				instance = new BoundaryDS("boundary");
				return instance;
			}
			else {
				return instance;
			}
		}
}	
