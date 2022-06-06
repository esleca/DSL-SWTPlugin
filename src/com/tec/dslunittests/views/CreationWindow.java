package com.tec.dslunittests.views;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.tec.dslunittests.models.Expected;
import com.tec.dslunittests.models.Parameter;
import com.tec.dslunittests.models.UnitTestData;

public class CreationWindow {

	private Group formGroup;
	private Button saveBtn, cleanBtn, addBtn;
	private Label label, paramListLbl;
	private Text nameTxt, functionTxt, expectedTxt, parametersTxt, valueTxt, assertTxt;
	private Composite layer;
	private String selectedAssert, selectedAssertType, selectedParamType, path;
	private Gson gson;
	private UnitTestData data = new UnitTestData();
	private Combo assertTypesCb, assertCb;

	public CreationWindow() {

	}

	public CreationWindow(String path) {
		this.path = path;
	}

	/**
	 * Defines widgets and layout for the unit test creation window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 */
	public Composite render(Composite parent) {
		// Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		layer.setLayout(new GridLayout(3, false));

		// JSON object
		gson = new GsonBuilder().setPrettyPrinting().create();

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

		// Displays information about the location of the unit test
		label = new Label(left, SWT.NONE);
		label.setText("Class name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(left, SWT.NONE);
		label.setText(getClassName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		// Form to define the parameters of the unit test
		formGroup = new Group(center, SWT.FILL);
		formGroup.setText("New unit test");
		GridLayout formLayout = new GridLayout(5, true);
		formLayout.horizontalSpacing = 10;
		formLayout.marginTop = 50;
		formGroup.setLayout(formLayout);
		formGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Function name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));

		functionTxt = new Text(formGroup, SWT.BORDER);
		functionTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		functionTxt.setText("");

		label = new Label(formGroup, SWT.NONE);
		label.setText("Test name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));

		nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		nameTxt.setText(getUTName());

		label = new Label(formGroup, SWT.NONE);
		label.setText("Parameter:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Create a dropdown Combo & Read only
		Combo typesCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available asserts
		String[] types = new String[] { "integer", "string", "boolean", "char", "double", "float" };
		typesCb.setItems(types);
		typesCb.setText("Data type");

		// User select a item in the Combo.
		typesCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = typesCb.getSelectionIndex();
				selectedParamType = typesCb.getItem(idx);
			}
		});

		parametersTxt = new Text(formGroup, SWT.BORDER);
		parametersTxt.setMessage("name");
		parametersTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		valueTxt = new Text(formGroup, SWT.BORDER);
		valueTxt.setMessage("value");
		valueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		// Clears the data of the form
		addBtn = new Button(formGroup, SWT.PUSH);
		addBtn.setText("+");
		addBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		addBtn.addListener(SWT.Selection,
				event -> addParameter(parametersTxt.getText(), selectedParamType, valueTxt.getText()));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Create a dropdown Combo & Read only
		assertCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available asserts
		String[] asserts = new String[] { "IsNull", "IsTrue", "IsFalse", "AreEqual", "AreNotEqual", "IsInstanceOf" };
		assertCb.setItems(asserts);
		assertCb.setText("Assert");

		// Create a dropdown Combo & Read only
		assertTypesCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available asserts
		assertTypesCb.setItems(types);
		assertTypesCb.setText("Data type");
		assertTypesCb.setVisible(false);

		// User select a item in the Combo.
		assertTypesCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = assertTypesCb.getSelectionIndex();
				selectedAssertType = assertTypesCb.getItem(idx);
			}
		});

		// User select a item in the Combo.
		assertCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = assertCb.getSelectionIndex();
				String selected = assertCb.getItem(idx);
				if (selected.equals("IsNull") || selected.equals("IsTrue") || selected.equals("IsFalse")) {
					selectedAssert = selected;
					assertTypesCb.setVisible(false);
					expectedTxt.setVisible(false);
					expectedTxt.setText("");
				} else {
					selectedAssert = selected;
					assertTypesCb.setVisible(true);
					expectedTxt.setVisible(true);
					expectedTxt.setText("");

				}
			}
		});

		expectedTxt = new Text(formGroup, SWT.BORDER);
		expectedTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		expectedTxt.setMessage("value");
		expectedTxt.setVisible(false);

		label = new Label(formGroup, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		paramListLbl = new Label(formGroup, SWT.NONE);
		paramListLbl.setText(" ");
		paramListLbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 5, 1));

		label = new Label(formGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));

		// Saves the unit test information
		saveBtn = new Button(formGroup, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		saveBtn.addListener(SWT.Selection, event -> save(layer.getShell()));

		label = new Label(formGroup, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		// Clears the data of the form
		cleanBtn = new Button(formGroup, SWT.PUSH);
		cleanBtn.setText("Clean");
		cleanBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		cleanBtn.addListener(SWT.Selection, event -> clear());

		// Modifies dynamically the size of the columns on resize of the main window
		parent.addListener(SWT.Resize, arg0 -> {
			Point size = parent.getSize();

			leftData.widthHint = (int) (size.x * 0.25);
			rightData.widthHint = (int) (size.x * 0.25);
			centerData.widthHint = size.x - leftData.widthHint - rightData.widthHint;

			formLayout.marginWidth = (int) (size.x * 0.015);
			;
		});

		formGroup.pack();

		return layer;
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

	private void save(Shell parent) {
		data.setClassName(getClassName());
		data.setFunctionName(functionTxt.getText());
		Expected exp = new Expected(selectedAssertType, expectedTxt.getText());
		data.setExpected(exp);
		data.setAssertType(selectedAssert);
		MessageBox dialog = new MessageBox(parent, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Creation confirmation");
		dialog.setMessage("Are you sure you want to submit unit test?");

		// open dialog and await user selection
		int returnCode = dialog.open();
		
		if (returnCode == 32) {
			try {
				Writer writer = new FileWriter(
						"D:\\TEC\\2022\\I semestre\\Asistencia\\DSL-SWTPlugin\\src\\com\\tec\\dslunittests\\resources\\package.json");
				gson.toJson(data, writer);
				writer.flush(); // flush data to file <---
				writer.close(); // close writer <---
				
				MessageBox msg = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
				msg.setText("Creation confirmation");
				msg.setMessage("New unit test was created sucessfully");
				msg.open();

			} catch (JsonIOException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void clear() {
		nameTxt.setText(getUTName());
		functionTxt.setText("");

		paramListLbl.setText("");
		expectedTxt.setText("");
		expectedTxt.setVisible(false);
		expectedTxt.setText("");
		assertTypesCb.setVisible(false);

	}

	private String getUTName() {
		return "Random name";
	}

	private void addParameter(String name, String type, String value) {
		Parameter newParam = new Parameter();
		newParam.setName(name);
		newParam.setType(type);
		newParam.setValue(value);
		data.getParameters().add(newParam);

		parametersTxt.setText("");
		selectedParamType = "";
		valueTxt.setText("");

		paramListLbl.setText(paramListLbl.getText() + ", " + newParam.getName() + " " + newParam.getType() + " "
				+ newParam.getValue());

		makeBold(paramListLbl);
		paramListLbl.pack();
	}

	private String getClassName() {
		// System.out.println(System.getProperty("file.separator"));
		// String[] parts = path.split(System.getProperty("file.separator"));
		File file = new File(path);
		String str = file.getName();
		return str.substring(0, str.lastIndexOf('.'));
	}
}
