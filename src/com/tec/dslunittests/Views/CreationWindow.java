package com.tec.dslunittests.Views;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreationWindow {

	private Group formGroup;
	private Button saveBtn, cleanBtn;
	private Label label;

	public Composite render(Composite parent) {

		parent.setLayout(new GridLayout(3, false));

		Composite left = new Composite(parent, SWT.NONE);
		Composite center = new Composite(parent, SWT.NONE);
		Composite right = new Composite(parent, SWT.NONE);

		GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData centerData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData rightData = new GridData(SWT.FILL, SWT.FILL, true, true);

		left.setLayoutData(leftData);
		center.setLayoutData(centerData);
		right.setLayoutData(rightData);

		GridLayout sideLayout = new GridLayout(2, true);
		left.setLayout(sideLayout);
		right.setLayout(sideLayout);

		FillLayout centerLayout = new FillLayout(SWT.VERTICAL);
		center.setLayout(centerLayout);

		parent.addListener(SWT.Resize, arg0 -> {
			Point size = parent.getSize();

			leftData.widthHint = (int) (size.x * 0.25);
			rightData.widthHint = (int) (size.x * 0.25);
			centerData.widthHint = size.x - leftData.widthHint - rightData.widthHint;
		});

		label = new Label(left, SWT.NONE);
		label.setText("Package name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(left, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		label = new Label(left, SWT.NONE);
		label.setText("Class name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(left, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		label = new Label(left, SWT.NONE);
		label.setText("Function name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(left, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		formGroup = new Group(center, SWT.FILL);
		formGroup.setText("New unit test");
		GridLayout formLayout = new GridLayout(2, true);
		formLayout.horizontalSpacing = 50;
		formLayout.marginWidth = 150;
		formLayout.marginTop = 50;
		formGroup.setLayout(formLayout);
		formGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Test name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);
		

		Text nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Parameters:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		Text parametersTxt = new Text(formGroup, SWT.BORDER);
		parametersTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		Text expectedTxt = new Text(formGroup, SWT.BORDER);
		expectedTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		label = new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));

		saveBtn = new Button(formGroup, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		saveBtn.addListener(SWT.Selection, event -> click());

		cleanBtn = new Button(formGroup, SWT.PUSH);
		cleanBtn.setText("Clean");
		cleanBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cleanBtn.addListener(SWT.Selection, event -> click());

		formGroup.pack();

		return parent;
	}

	private void makeBold(Label label) {
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(label.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(label.getDisplay());
		label.setFont(boldFont);
	}
	
	private void click() {
		
	}

}
