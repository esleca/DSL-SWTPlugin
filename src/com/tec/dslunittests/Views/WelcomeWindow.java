package com.tec.dslunittests.Views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class WelcomeWindow {
	
	private Button newUTBtn, editUTBtn;
	private Composite layer;
	private CTabFolder folder;
	/**
	 * Defines widgets and layout for the welcome window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 */
	public Composite render(Composite parent) {
		//Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		layer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		//Creates the folder to contain tabs
		folder = new CTabFolder(layer, SWT.BORDER);

		//Default tab with welcome information
		CTabItem item1 = new CTabItem(folder, SWT.CLOSE);
		item1.setText("Unit tests");
		folder.setSelection(0);
	

		// Loads new unit test window
		newUTBtn = new Button(folder, SWT.PUSH);
		newUTBtn.setText("Add unit test");
		newUTBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		newUTBtn.addListener(SWT.Selection, event -> loadCreationWindow(folder));
		

		// Load edit unit test window
		editUTBtn = new Button(folder, SWT.PUSH);
		editUTBtn.setText("Edit");
		editUTBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		editUTBtn.addListener(SWT.Selection, event -> loadEditionWindow(folder));
		
		item1.setControl(newUTBtn);
		

		return layer;
	}

	private void loadCreationWindow(Composite parent) {

		int idx = folder.getSelectionIndex();
		
		//Creates new tab to display content
        CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
        item.setText("New unit test");
        
		//Creates window to create individual unit tests
		CreationWindow creationWindow = new CreationWindow();
		//Renders content in new tab
		item.setControl(creationWindow.render(parent));
		
		//Refreshes view
		parent.requestLayout();
	}

	private void loadEditionWindow(Composite parent) {
		
		int idx = folder.getSelectionIndex();

		//Creates new tab item for content of the window
        CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
        item.setText("Edit unit test");
		//Creates window to create individual unit tests
		EditionWindow editionWindow = new EditionWindow();
		editionWindow.render(editionWindow.render(parent));
		parent.requestLayout();
	}

}
