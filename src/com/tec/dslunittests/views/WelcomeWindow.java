package com.tec.dslunittests.views;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class WelcomeWindow {

	private Button newUTBtn, editUTBtn, packageBtn, classBtn, functionBtn;
	private Composite layer, page;
	private CTabFolder folder;

	/**
	 * Defines widgets and layout for the welcome window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 */
	public Composite render(Composite parent) {
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

		// Loads new unit test window
		newUTBtn = new Button(page, SWT.PUSH);
		newUTBtn.setText("Add unit test");
		newUTBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		newUTBtn.addListener(SWT.Selection, event -> loadCreationWindow(folder));

		// Load package unit test window
		packageBtn = new Button(page, SWT.PUSH);
		packageBtn.setText("Package");
		packageBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		packageBtn.addListener(SWT.Selection, event -> {
			try {
				loadPackageWindow(folder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// Load class unit test window
		classBtn = new Button(page, SWT.PUSH);
		classBtn.setText("Class");
		classBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		classBtn.addListener(SWT.Selection, event -> loadCreationWindow(folder));

		// Load function unit test window
		functionBtn = new Button(page, SWT.PUSH);
		functionBtn.setText("Function");
		functionBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		functionBtn.addListener(SWT.Selection, event -> loadCreationWindow(folder));

		item1.setControl(page);

		return layer;
	}

	private void loadCreationWindow(Composite parent) {

		int idx = folder.getSelectionIndex();

		// Creates new tab to display content
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("New unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		CreationWindow creationWindow = new CreationWindow();
		// Renders content in new tab
		item.setControl(creationWindow.render(parent));

		// Refreshes view
		parent.requestLayout();
	}


	private void loadPackageWindow(Composite parent) throws IOException {

		int idx = folder.getSelectionIndex();

		// Creates new tab to display content
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("Package unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		PackageUnitTestWindow packageWindow = new PackageUnitTestWindow();
		// Renders content in new tab
		item.setControl(packageWindow.render(parent));

		// Refreshes view
		parent.requestLayout();
	}

}
