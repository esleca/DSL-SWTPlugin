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
	private Text nameTxt, expectedTxt, parametersTxt;
	private Composite layer;

	/**
	 * Defines widgets and layout for the unit test creation window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 */
	public Composite render(Composite parent) {
		//Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		layer.setLayout(new GridLayout(3, false));

		// Columns to divide the view
		Composite left = new Composite(layer, SWT.NONE);
		Composite center = new Composite(layer, SWT.NONE);
		Composite right = new Composite(layer, SWT.NONE);

		// Alignment of the widgets inside columns
		GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData centerData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData rightData = new GridData(SWT.FILL, SWT.FILL, true, true);

		left.setLayoutData(leftData);
		center.setLayoutData(centerData);
		right.setLayoutData(rightData);

		// Setting layouts for the composite columns
		GridLayout sideLayout = new GridLayout(2, true);
		left.setLayout(sideLayout);
		right.setLayout(sideLayout);

		FillLayout centerLayout = new FillLayout(SWT.VERTICAL);
		center.setLayout(centerLayout);

		// Modifies dynamically the size of the columns on resize of the main window
		parent.addListener(SWT.Resize, arg0 -> {
			Point size = parent.getSize();

			leftData.widthHint = (int) (size.x * 0.25);
			rightData.widthHint = (int) (size.x * 0.25);
			centerData.widthHint = size.x - leftData.widthHint - rightData.widthHint;
		});

		// Displays information about the location of the unit test
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

		// Form to define the parameters of the unit test
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

		nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		nameTxt.setText(getUTName());

		label = new Label(formGroup, SWT.NONE);
		label.setText("Parameters:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		parametersTxt = new Text(formGroup, SWT.BORDER);
		parametersTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		expectedTxt = new Text(formGroup, SWT.BORDER);
		expectedTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		label = new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));

		// Saves the unit test information
		saveBtn = new Button(formGroup, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		saveBtn.addListener(SWT.Selection, event -> save());

		// Clears the data of the form
		cleanBtn = new Button(formGroup, SWT.PUSH);
		cleanBtn.setText("Clean");
		cleanBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cleanBtn.addListener(SWT.Selection, event -> clear());

		formGroup.pack();

		return parent;
	}

	/**
	 * Miscellaneous function to transform text of a label
	 *
	 * @param label to apply the transformation
	 * @return
	 */
	private void makeBold(Label label) {
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(label.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(label.getDisplay());
		label.setFont(boldFont);
	}

	private void save() {
		String name = nameTxt.getText();
		String parameters = parametersTxt.getText();
		String expected = expectedTxt.getText();
		System.out.println("---- New unit test ----");
		System.out.println("Name: " + name);
		System.out.println("Parameters: " + parameters);
		System.out.println("Expected: " + expected);
	}

	private void clear() {
		nameTxt.setText(getUTName());
		parametersTxt.setText("");
		expectedTxt.setText("");
	}

	private String getUTName() {
		return "Random name";
	}

}
