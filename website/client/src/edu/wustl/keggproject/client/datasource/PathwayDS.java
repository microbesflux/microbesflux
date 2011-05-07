package edu.wustl.keggproject.client.datasource;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;

import edu.wustl.keggproject.client.ConfigurationFactory;

public class PathwayDS extends RestDataSource {
	private static PathwayDS instance = null;
	private static String myurl = ConfigurationFactory.getConfiguration()
			.getBaseURL() + "pathway/";

	private PathwayDS(String id) {
		setID(id);

		// Cross domain JSON
		setDataFormat(DSDataFormat.JSON);
		setDataTransport(RPCTransport.SCRIPTINCLUDE);
		setCallbackParam("callback");

		DataSourceIntegerField pkField = new DataSourceIntegerField("pk");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);

		DataSourceField ko = new DataSourceField("ko", FieldType.BOOLEAN, "KO");
		DataSourceField reactionid = new DataSourceField("reactionid",
				FieldType.TEXT, "Reaction");
		DataSourceField reactants = new DataSourceField("reactants",
				FieldType.TEXT, "Reactants");
		DataSourceField arrow = new DataSourceField("arrow", FieldType.TEXT,
				"Arrow");
		DataSourceField products = new DataSourceField("products",
				FieldType.TEXT, "Products");
		DataSourceTextField pathway = new DataSourceTextField("pathway",
				"Pathway");
		// pathway.setHidden(true);
		
		setFields(pkField, ko, reactionid, reactants, arrow, products, pathway);
		setFetchDataURL(myurl + "fetch/");
		setUpdateDataURL(myurl + "update/");
		setAddDataURL(myurl + "add/");
		setRemoveDataURL(myurl + "fetch/"); // not used
	}

	public static PathwayDS getInstance() {
		if (instance == null) {
			instance = new PathwayDS("model");
			return instance;
		} else {
			return instance;
		}
	}
}
