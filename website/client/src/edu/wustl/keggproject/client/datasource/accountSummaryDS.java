package edu.wustl.keggproject.client.datasource;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;

import edu.wustl.keggproject.client.ConfigurationFactory;

public class accountSummaryDS extends RestDataSource {
	private static accountSummaryDS instance = null;
	private static String myurl = ConfigurationFactory.getConfiguration()
			.getBaseURL() + "pathway/";

	private accountSummaryDS(String id) {
		setID(id);

		// Cross domain JSON
		setDataFormat(DSDataFormat.JSON);
		setDataTransport(RPCTransport.SCRIPTINCLUDE);
		setCallbackParam("callback");

		DataSourceIntegerField pkField = new DataSourceIntegerField("pk");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);

		DataSourceField date = new DataSourceField("date", FieldType.TEXT,
				"Date");
		DataSourceField model = new DataSourceField("model", FieldType.TEXT,
				"Model");
		DataSourceField type = new DataSourceField("type", FieldType.TEXT,
				"Type");
		DataSourceField status = new DataSourceField("status", FieldType.TEXT,
				"Status");

		setFields(pkField, date, model, type, status);

		setFetchDataURL(myurl + "fetch/"); // TODO
		setUpdateDataURL(myurl + "update/");// TODO
		setAddDataURL(myurl + "add/");// TODO
		setRemoveDataURL(myurl + "fetch/"); // TODO
	}

	public static accountSummaryDS getInstance() {
		if (instance == null) {
			instance = new accountSummaryDS("model");// TODO
			return instance;
		} else {
			return instance;
		}
	}
}
