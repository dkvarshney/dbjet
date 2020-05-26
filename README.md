# dbjet
A simple but powerful database ORM library for Java.


## The JET MODEL - Your persistable model.
  ```
  @JetTable(name = "ACCOUNT_TABLE")
  public class AccountModel implements JetModel {	
	@JetColumn(name = "UID")
	@JetColumnIsPrimary
	private String id;
	
	@JetColumn(name = "NAME")
	@JetColumnUpdatable	
	private String name;
	
	@JetColumn(name = "EMAIL")
	@JetColumnUpdatable
	@JetColumnIsIndex
	@JetColumnIsUnique
	private String email;
	
	@JetColumn(name = "KEY_BASE64", marshaller = JetBase64Marshaller.class)
	@JetColumnUpdatable	
	private String key;
 }
```

## The JET MANAGER
```
  public class AccountManager extends JetAbstractManager<AccountModel> {
    // Any method you may want to add. 
    // Following methods are available by default:
    public <Model> Get(Column);
    public <Model> Delete(Column);
    public <Model> Post(Model);      
    public void update(Column, Model);
    public List<Model>search(Criteria, Controls);
  }
```

## The Uses
```
  // enable table creation as per the model.
  JetInitializer.initialize(config, true);
		
  // init the manager.
  AccountManager manager = new AccountManager();
  AccountModel model = manager.create(model);
  AccountModel model = manager.get(<ID>);  
```

Check the test case (https://github.com/dkvarshney/dbjet/blob/master/dbjet-engine/src/test/java/dk/dbjet/TestJetManager.java) for more details.


## The Marshaller
  ``` @JetColumn(name = "MY_COLUMN", marshaller = JetBase64Marshaller.class) ```
  Any Marshaller which can transcode your data for any column.
  For Example: JetBase64Marshaller (as above example) will encode the column key to base64 before persisting it to database and do base64 decode after fecthing it from database and before returning it to caller.
  Your data will be persisated as base64 encoded in database and will be auto decoded on making any get or search call.
  
  Custom Marshllers:
```  
public class MyAwesomeMarshaller implements JetMarshaller <String> {	
  @Override
  public String marshal(JetColumn column, String value) {
    // return final data to store in database. An example would be to encrypt the data.
  }
  @Override
  public String unmarshal(JetColumn column, String value) {	
    // return raw data to return. An example would be to decrypt the data.  
  }
}
```    

And uses:

``` @JetColumn(name = "MY_COLUMN", marshaller = MyAwesomeMarshaller.class) ```

Check https://github.com/dkvarshney/dbjet/blob/master/dbjet-common/src/main/java/dk/dbjet/marshaller/JetBase64Marshaller.java for more details.
