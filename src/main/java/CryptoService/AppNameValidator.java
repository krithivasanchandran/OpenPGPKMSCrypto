package CryptoService;

import java.util.logging.Logger;

public class AppNameValidator {
	
	private final static Logger logger = Logger.getLogger(AppNameValidator.class.getName());
	
	private String appName;
	
	AppNameValidator(String enteredAppName){
		
		if(enteredAppName != null || !enteredAppName.isEmpty()){

			logger.info("Initializing AppNameValidator.java Class");
			logger.info("AppName Entered from Arguments is " + enteredAppName);
			this.appName = enteredAppName.trim();
			
			logger.exiting(AppNameValidator.class.getName(), "Constructor :: AppNameValidator");
		}else {
			logger.severe(" enteredAppName => empty || Null, Now Exiting" );
			logger.exiting(AppNameValidator.class.getName(), "Constructor :: AppNameValidator :: Exiting");
			System.exit(0);
		}
	}
	
	public boolean isValid(){
		logger.entering(AppNameValidator.class.getName(), "isValid::Method");
		
		if(appName instanceof String){
			logger.info("AppName is Instance Of String" + AppNameValidator.class.getName());
			
			if(!containsSpecialCharacters(appName)){
				logger.info("Success !! The AppName doesnot consist of any Special Characters");
				return true;
			}
		}
		return false;
	}
	
	private boolean containsSpecialCharacters(String applicationName){
		
		logger.entering(AppNameValidator.class.getName(), "containsSpecialCharacters::Method");
		
		String specialCharacters = "!#$%&'()*+,/:;<=>?@[]^`{|}~";
		char[] spCharArr = specialCharacters.toCharArray();
		
		logger.info("Special Characters against which the AppName is tested for" + specialCharacters);
		
	    int appNameLength = applicationName.length();
	    
	    for(int i=0;i<spCharArr.length;i++){
	    	
	    	char c = spCharArr[i];
	    	for(int j=0;j<appNameLength;j++){
	    		
	    	  if(c == applicationName.charAt(j)){
	    		  logger.severe("AppName consists of Special Characters which is unacceptable.");
	    		  return true;
	    	  }
	    	}
	    }
		return false;
	}

}
