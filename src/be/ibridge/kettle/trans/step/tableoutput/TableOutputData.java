 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** It belongs to, is maintained by and is copyright 1999-2005 by     **
 **                                                                   **
 **      i-Bridge bvba                                                **
 **      Fonteinstraat 70                                             **
 **      9400 OKEGEM                                                  **
 **      Belgium                                                      **
 **      http://www.kettle.be                                         **
 **      info@kettle.be                                               **
 **                                                                   **
 **********************************************************************/
 

package be.ibridge.kettle.trans.step.tableoutput;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Map;

import be.ibridge.kettle.core.database.Database;
import be.ibridge.kettle.trans.step.BaseStepData;
import be.ibridge.kettle.trans.step.StepDataInterface;


/**
 * @author Matt
 * @since 24-jan-2005
 */
public class TableOutputData extends BaseStepData implements StepDataInterface
{
	public  Database db;
	public  int      warnings;
    
    /**
     * Mapping between the SQL and the actual prepared statement.
     * Normally this is only one, but in case we have more then one, it's convenient to have this.
     */
    public  Map      preparedStatements;
    
    public  int      indexOfPartitioningField;
    
    /** Cache of the data formatter object */
    public SimpleDateFormat dateFormater;

    /** Use batch mode or not? */
    public boolean batchMode;
    
	public TableOutputData()
	{
		super();
		
		db=null;
		warnings=0;
        
        preparedStatements = new Hashtable(); 
        
        indexOfPartitioningField = -1;
	}

}
