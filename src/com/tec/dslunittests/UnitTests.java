package com.tec.dslunittests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

//import gestors.GestorDSL;
//import gestors.IGestorDSL;
//import processor.gastgateway.CompilationUnitHandler;
//import utils.ConsolePrinter;

import org.eclipse.ui.part.ViewPart;

public class UnitTests extends ViewPart {

	// private IGestorDSL dsl;

	private Button newUTBtn, packageBtn, classBtn, functionBtn;
	private Composite layer, page;
	private CTabFolder folder;

	public UnitTests() {
		// dsl = new GestorDSL( null );
	}

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

		item1.setControl(page);
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public CTabFolder getFolder() {
		return folder;
	}

}
