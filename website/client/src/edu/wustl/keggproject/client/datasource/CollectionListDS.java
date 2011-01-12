package edu.wustl.keggproject.client.datasource;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;

import edu.wustl.keggproject.client.Configuration;
import edu.wustl.keggproject.client.ConfigurationFactory;

public class CollectionListDS extends RestDataSource {
	private static CollectionListDS instance = null;
	private static String baseurl = ConfigurationFactory.getConfiguration().getBaseURL() + "collection/list/";
	
	private CollectionListDS(String id)
	{
			setID(id);		
			setDataFormat(DSDataFormat.JSON);
		    setDataTransport(RPCTransport.SCRIPTINCLUDE);
		    setCallbackParam("callback");
		    
			DataSourceField name = new DataSourceField("n", FieldType.TEXT, "Name");
			setFields(name);
			
			setFetchDataURL(baseurl);
	}
		
		public static CollectionListDS getInstance(){
			if (instance == null) {
				instance = new CollectionListDS("collectionlist");
				return instance;
			}
			else {
				return instance;
			}
		}
}	