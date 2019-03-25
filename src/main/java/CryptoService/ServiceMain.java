package CryptoService;

import java.util.Map;
import java.util.logging.Logger;

import KeyMakerService.AccessKeys;


public class ServiceMain {
	
	private final static Logger logger = Logger.getLogger(ServiceMain.class.getName());
	
	public Map<String,String> serviceValidator(String a[]) throws Exception{
		
		/*
		 * Argument 1 -> Application Name
		 */
		 String appName = a[0];
		
		/*
		 * Argument 2 -> Key Name
		 */
		 String keyname = a[1];
		
		/*
		 * Argument 3 -> Encryption / Decryption Operation
		 * Type -> "ENC" -> for Encryption
		 * Type -> "DEC" -> for Decryption
		 */
		
		 String OperationType = a[2]; 
		
		/*
		 * Argument 4 -> Input File Name 
		 * Absolute Path -> File Name
		 * Should Include the Root Directory
		 */
		 Object absoluteFilePath = a[3];
		
		System.out.println("Argument 0 :: AppName => " + appName);
		System.out.println("Argument 1 :: KeyName => " + keyname);
		System.out.println("Argument 2 :: OperationType => " + OperationType);
		System.out.println("Argument 3 :: absoluteFilePath => " + absoluteFilePath);

		
		/*
		 * Validate Application Name
		 */
		AppNameValidator validateAppObj = new AppNameValidator(appName);
		boolean isAppNameValid = validateAppObj.isValid();
		
		if(isAppNameValid){
			logger.entering(ServiceMain.class.getName(), "main::Method");
			
			boolean isKeyValid = (keyname != null) && !(keyname.isEmpty()) ? (keyname instanceof String) : false;
			
			if(isKeyValid){
				logger.info("KeyName is Valid and doesnt contain null or empty characters");
				
				if(isValidOperationType(OperationType)){
					logger.info("Recognition Successfull ==> Is a valid Operation Type");
					
					//Yet to add
					switch(OperationType){
					
					case "ENC":
						 System.out.println("Encryption Context Switch is called");
						 AccessKeys keyaccess = new AccessKeys();
						 return keyaccess.accessKeyMakerKeys(appName);
						
					case "DEC":
						System.out.println("Decryption Context Switch is called");
						AccessKeys key_1 = new AccessKeys();
						return key_1.accessKeyMakerKeys(appName);
						
					default:
						System.out.println("None of the switch case got executed !!");
					}
					
				}else{
					
					logger.info("Operation Type doesnot recognize the format");
					logger.severe("Please enter either ENC or DEC");
				}
			}else{
				logger.info("KeyName contains null or empty characters");
			}
		}else{
			logger.info("Critical !! Appname doesn't contain some valid characters, Please check for special characters ");
		}
		
		//final GenericFileValidator genericFileObj = new GenericFileValidator();
		//genericFileObj.isValidFile(absoluteFilePath);
		boolean isValidFile = true;
		
		if(isValidFile){
				
				logger.info("File check completed !!! It is a valid absolute file ");
				
			}else{
				
				logger.severe("File :: " + absoluteFilePath + " is not a valid absolute file path");
				logger.info("Application Exiting !! Please enter a valid File Path");
				System.exit(0);
			}
		return null;
		}

	public static boolean isValidOperationType(String OperationType){
		
		logger.entering(ServiceMain.class.getName(), "isValidOperationType::method");
		
		if(OperationType != null && !OperationType.isEmpty()){
			logger.info("Operation Type is not null and not empty");
			
			if(OperationType instanceof String){
				logger.info("Operation Type is instanceof String");
				
				boolean check = OperationType.equalsIgnoreCase("ENC") || OperationType.equalsIgnoreCase("DEC");
				return check;
			}else{
				
				logger.info("Operation Type is not an instance of String");
				return false;
			}
		}
		return false;
	}
}