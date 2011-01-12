package edu.wustl.keggproject.client.datasource;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;

import edu.wustl.keggproject.client.Configuration;
import edu.wustl.keggproject.client.ConfigurationFactory;


public class BoundaryDS extends RestDataSource {
	private static BoundaryDS _instance = null;
	private static String myurl = ConfigurationFactory.getConfiguration().getBaseURL() + "model/bound/";
	
	private BoundaryDS(String id)
	{
			setID(id);
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
						
			setFetchDataURL(myurl + "fetch/");
			setUpdateDataURL(myurl + "update/");
		}
		
		public static BoundaryDS getInstance(){
			if (_instance == null) {
				_instance = new BoundaryDS("boundary");
				return _instance;
			}
			else {
				return _instance;
			}
		}
}	
