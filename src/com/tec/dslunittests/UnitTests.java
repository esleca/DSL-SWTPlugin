package com.tec.dslunittests;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.tec.dslunittests.models.Message;
import com.tec.dslunittests.models.UnitTestRequest;
import com.tec.dslunittests.resources.Constants;

public class UnitTests extends ViewPart {

	private Composite layer, page;
	private CTabFolder folder;

	/**
	 * Defines widgets and layout for the welcome window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		layer.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Creates the folder to contain tabs
		folder = new CTabFolder(layer, SWT.BORDER);

		// Default tab with welcome information
		CTabItem item1 = new CTabItem(folder, SWT.CLOSE);
		item1.setText("Unit tests");
		folder.setSelection(0);

		page = new Composite(folder, SWT.NONE);
		page.setLayout(new FillLayout(SWT.HORIZONTAL));

		// Adds new parameter to the list
		Button addBtn = new Button(page, SWT.PUSH);
		addBtn.setText("+");
		addBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		addBtn.addListener(SWT.Selection, event -> sendRequest());

		item1.setControl(page);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public CTabFolder getFolder() {
		return folder;
	}

	public void sendRequest() {
		try {
			Socket socket = new Socket(Constants.hostName, Constants.portNumber);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			
			 // make a bunch of messages to send.
	        Message msg = new Message("CREATE", null, "");
	        objectOutputStream.writeObject(msg);
	        objectOutputStream.close();			
	        socket.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
