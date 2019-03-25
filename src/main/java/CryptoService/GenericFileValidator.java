package CryptoService;

import java.io.File;

public class GenericFileValidator<T> {

	public boolean isValidFile(T t){
		
		if(t instanceof String){
			
			File inFileHandler = new File((String)t);
			
			/**
		     * Tests whether this abstract pathname is absolute.  The definition of
		     * absolute pathname is system dependent.  On UNIX systems, a pathname is
		     * absolute if its prefix is <code>"/"</code>.  On Microsoft Windows systems, a
		     * pathname is absolute if its prefix is a drive specifier followed by
		     * <code>"\\"</code>, or if its prefix is <code>"\\\\"</code>.
		     *
		     * @return  <code>true</code> if this abstract pathname is absolute,
		     *          <code>false</code> otherwise
		     */
			
		    boolean isValidFile = inFileHandler.isFile() && inFileHandler.isAbsolute() 
		    		                   && inFileHandler.canRead() && inFileHandler.canWrite()
		    		                    && inFileHandler.canExecute();
		    
		    return isValidFile;
			
		}
		
		return false;
	}
	
}
