package edu.wustl.keggproject.client.datasource;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;

import edu.wustl.keggproject.client.ConfigurationFactory;

public class ConstraintDS extends RestDataSource {
	private static ConstraintDS instance = null;
	private static String baseurl = ConfigurationFactory.getConfiguration()
			.getBaseURL() + "model/sv/";

	private ConstraintDS(String id) {
		setID(id);
		// Cross domain JSON
		setDataFormat(DSDataFormat.JSON);
		setDataTransport(RPCTransport.SCRIPTINCLUDE);
		setCallbackParam("callback");

		DataSourceIntegerField pkField = new DataSourceIntegerField("pk");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);

		DataSourceField reactionid = new DataSourceField("r", FieldType.TEXT,
				"Constraints");
		DataSourceField compound = new DataSourceField("c", FieldType.TEXT,
				"Compound");
		setFields(pkField, reactionid, compound);

		setFetchDataURL(baseurl + "fetch/");
	}

	public static ConstraintDS getInstance() {
		if (instance == null) {
			instance = new ConstraintDS("constraints");
			return instance;
		} else {
			return instance;
		}
	}
}
