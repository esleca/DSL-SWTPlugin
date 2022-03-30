package com.tec.dslunittests;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


public class HelloWorld extends ViewPart {

	public HelloWorld() {
	}

	@Override
	public void createPartControl(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		button.setText("Say Hello");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(parent.getShell(), "Say Hello Button", getContentDescription());
			}
		});
		
		
	}

	@Override
	public void setFocus() {

	}

}
