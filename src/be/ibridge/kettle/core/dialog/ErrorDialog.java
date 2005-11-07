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
 
package be.ibridge.kettle.core.dialog;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import be.ibridge.kettle.core.Const;
import be.ibridge.kettle.core.GUIResource;
import be.ibridge.kettle.core.Props;
import be.ibridge.kettle.core.WindowProperty;
import be.ibridge.kettle.core.exception.KettleException;


/**
 * Dialog to display an error generated by a Kettle Exception.
 * 
 * @author Matt
 * @since 19-06-2003
 */
public class ErrorDialog extends Dialog
{
	private Label        wlDesc;
	private Text         wDesc;
    private FormData     fdlDesc, fdDesc;
		
	private Button wOK;
	private FormData fdOK;
	private Listener lsOK;

	private Shell  shell;
	private SelectionAdapter lsDef;
	private Props props;
	
	public ErrorDialog(Shell parent, Props props, String title, String message)
	{
		this(parent, props, title, message, null);
	}

	public ErrorDialog(Shell parent, Props props, String title, String message, Exception exception)
	{
		super(parent, SWT.NONE);
		this.props     = props;

		Display display  = parent.getDisplay();
		final Color gray = GUIResource.getInstance().getColorLightGray();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN | SWT.APPLICATION_MODAL);
 		props.setLook(shell);

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(title);
		
		int margin = Const.MARGIN;

		// From step line
		wlDesc=new Label(shell, SWT.NONE);
		wlDesc.setText(message);
 		props.setLook(wlDesc);
		fdlDesc=new FormData();
		fdlDesc.left = new FormAttachment(0, 0);
		fdlDesc.top  = new FormAttachment(0, margin);
		wlDesc.setLayoutData(fdlDesc);
		
		wDesc=new Text(shell, SWT.MULTI  | SWT.LEFT | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
		if (exception!=null) 
		{
			String text = "";
			
			if (exception instanceof KettleException) // Normal error
			{
				KettleException ke = (KettleException) exception;
				text = ke.getMessage();
				text += Const.CR;
				text += Const.CR;
			}
			else
            // Error from somewhere else, what is the cause?
			if (exception instanceof InvocationTargetException) 
			{
				Throwable cause = exception.getCause();
				if (cause instanceof KettleException)
				{
					KettleException ke = (KettleException)cause;
					text = ke.getMessage();
					text += Const.CR;
					text += Const.CR;
				}
				else
				{
					text = Const.NVL(cause.getMessage(), cause.toString());
					while (text==null && cause!=null)
					{
						cause = cause.getCause();
						if (cause!=null) 
						{
							text = Const.NVL(cause.getMessage(), cause.toString());
						}
					}
					
					text += Const.CR;
					text += Const.CR;
				}
			}
			else
			if (exception instanceof Throwable) // Error from somewhere else...
			{
				text = exception.getMessage();
				text += Const.CR;
				text += Const.CR;
			}

			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			
			text+=sw.getBuffer().toString();
			
			wDesc.setText( text );
		}
		wDesc.setBackground(gray);
		fdDesc=new FormData();
		fdDesc.left  = new FormAttachment(0, 0);
		fdDesc.top   = new FormAttachment(wlDesc, margin);
		fdDesc.right = new FormAttachment(100, 0);
		fdDesc.bottom= new FormAttachment(100, -50);
		wDesc.setLayoutData(fdDesc);
		wDesc.setEditable(false);

		wOK=new Button(shell, SWT.PUSH);
		wOK.setText("  &Close  ");
		fdOK=new FormData();
		fdOK.left       = new FormAttachment(50, 0);
		fdOK.bottom     = new FormAttachment(100, 0);
		wOK.setLayoutData(fdOK);

		// Add listeners
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		wOK.addListener    (SWT.Selection, lsOK     );
		
		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		wDesc.addSelectionListener(lsDef);
		
		// Detect [X] or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { ok(); } } );
		// Clean up used resources!
		shell.addDisposeListener(new DisposeListener() 
			{
				public void widgetDisposed(DisposeEvent arg0) 
				{
				}
			}
		);
		
		WindowProperty winprop = props.getScreen(shell.getText());
		if (winprop!=null) winprop.setShell(shell); else shell.pack();

		shell.open();
		while (!shell.isDisposed())
		{
				if (!display.readAndDispatch()) display.sleep();
		}
	}

	public void dispose()
	{
		props.setScreen(new WindowProperty(shell));
		shell.dispose();
	}
	
	private void ok()
	{
		dispose();
	}
}
