package edu.wustl.keggproject.client;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.RPCTransport;


public class JSONDS extends RestDataSource{
	private static JSONDS instance = null;
	
	private JSONDS(String id)
	{
		setID(id);
		
		// Cross domain JSON
		setDataFormat(DSDataFormat.JSON);
	    setDataTransport(RPCTransport.SCRIPTINCLUDE);
	    setCallbackParam("callback");
	    
		DataSourceIntegerField pkField = new DataSourceIntegerField("pk");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceField ko 		  = new DataSourceField("ko", FieldType.BOOLEAN, "KO");
		DataSourceField reactants = new DataSourceField("reactants", FieldType.TEXT, "Reactants");
		DataSourceField arrow     = new DataSourceField("arrow", FieldType.TEXT, "Arrow");
		DataSourceField products  = new DataSourceField("products", FieldType.TEXT, "Products");
		DataSourceTextField pathway =  new DataSourceTextField("pathway", "Pathway");
		pathway.setHidden(true);
		// pathway.setValueMap("")
		setFields(pkField, ko, reactants, arrow, products, pathway);
		
		setFetchDataURL("http://www.cse.wustl.edu/~yx2/fetch.py");
		setUpdateDataURL("http://www.cse.wustl.edu/~yx2/update.py");
		setAddDataURL("http://www.cse.wustl.edu/~yx2/add.py");
		setRemoveDataURL("http://www.cse.wustl.edu/~yx2/fetch.py");
	}
	
	public static JSONDS getInstance(){
		if (instance == null) {
			instance = new JSONDS("optimization");
			return instance;
		}
		else {
			return instance;
		}
	}
}

