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
 

package be.ibridge.kettle.trans.step.sql;

import java.util.List;

import be.ibridge.kettle.core.Result;
import be.ibridge.kettle.core.database.Database;
import be.ibridge.kettle.trans.step.BaseStepData;
import be.ibridge.kettle.trans.step.StepDataInterface;


/**
 * @author Matt
 * @since 20-jan-2005
 */
public class ExecSQLData extends BaseStepData implements StepDataInterface
{
	public Database db;
    public Result   result;
    public int[]    argumentIndexes;
    public List     markerPositions;
	
	public ExecSQLData()
	{
		super();
		
		db              = null;
        result          = null;
        argumentIndexes = null;
        markerPositions = null;
	}


	
}
