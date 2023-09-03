package com.tec.dslunittests.views;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.dsl.models.parameters.ParameterScenario;
import com.dsl.models.unittests.UnitTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.tec.dslunittests.models.Expected;
import com.tec.dslunittests.models.Parameter;
import com.tec.dslunittests.models.UnitTestRequest;
import com.tec.dslunittests.resources.Constants;

public class EditionWindow {

	private List<Parameter> currentParameters;
	private Group formGroup;
	private Button saveBtn, cancelBtn, addBtn;
	private Label label, paramListLbl;
	private Text nameTxt, functionTxt, expectedTxt, newParameterTxt, newParameterValueTxt, assertionTxt;
	private Composite layer;
	private String selectedAssertion, selectedExpectedType, selectedNewParamType, path;
	private Gson gson;
	private UnitTestRequest testData;
	private Combo expectedTypeCb, assertionsCb;
	
	/**
	 * Defines widgets and layout for the unit test edition window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 */
	public Composite render(Composite parent, UnitTestRequest testData) {
		this.testData = testData;
		
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
		label.setText(testData.getClass().getName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		label = new Label(left, SWT.NONE);
		label.setText("Function name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(left, SWT.NONE);
		label.setText(testData.getTestName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));

		// Form to define the parameters of the unit test
		formGroup = new Group(center, SWT.FILL);
		formGroup.setText("Edit unit test");
		GridLayout formLayout = new GridLayout(5, true);
		formLayout.horizontalSpacing = 10;
		formLayout.marginTop = 50;
		formGroup.setLayout(formLayout);
		formGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Test name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));

		nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		nameTxt.setText(testData.getTestName());
/*
		label = new Label(formGroup, SWT.NONE);
		label.setText("Parameters:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

				
		// Create a dropdown Combo & Read only
		Combo typesCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		typesCb.setItems(Constants.types);
		typesCb.select(0);

		// User select a item in the Combo.
		typesCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = typesCb.getSelectionIndex();
				selectedNewParamType = typesCb.getItem(idx);
			}
		});

		
		newParameterTxt = new Text(formGroup, SWT.BORDER);
		newParameterTxt.setMessage("Parameter name");
		newParameterTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		newParameterValueTxt = new Text(formGroup, SWT.BORDER);
		newParameterValueTxt.setMessage("Parameter value");
		newParameterValueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		// Adds new parameter to the list
		addBtn = new Button(formGroup, SWT.PUSH);
		addBtn.setText("+");
		addBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		addBtn.addListener(SWT.Selection,
				event -> addParameter(newParameterTxt.getText(), selectedNewParamType, newParameterValueTxt.getText()));
		
		paramListLbl = new Label(formGroup, SWT.NONE);
		paramListLbl.setText(" "); */
		
		//Make all parameters editable
		currentParameters = testData.getParameters();
		//currentParameters = null;
		for(int i = 0; i < currentParameters.size(); i++) {
			
			label = new Label(formGroup, SWT.NONE);
			label.setText("");
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
			
					
			Combo paramType = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
			paramType.setItems(Constants.types);
			String type = currentParameters.get(i).getType();
			if ( type.equals("int")){
				paramType.select(0);
			}
			else if(type.equals("String"))
			{
				paramType.select(1);
			}
			else if(type.equals("boolean"))
			{
				paramType.select(2);
			}
			else if(type.equals("char"))
			{
				paramType.select(3);
			}
			else if(type.equals("double"))
			{
				paramType.select(4);
			}
			else if(type.equals("float"))
			{
				paramType.select(5);
			}
			else if(type.equals("long"))
			{
				paramType.select(6);
			}
			
			
			paramType.pack();
			
			final int index = i;

			// User select a item in the Combo.
			paramType.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int idx = paramType.getSelectionIndex();
					Parameter param = currentParameters.get(index);
					param.setType(paramType.getItem(idx));
					currentParameters.add(index, param);
				}
			});

			newParameterTxt = new Text(formGroup, SWT.BORDER);
			newParameterTxt.setText(currentParameters.get(i).getName());
			newParameterTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			
			//Listener to detect changes in text
			ModifyListener paramNameListener = new ModifyListener() {
			    /** {@inheritDoc} */
			    public void modifyText(ModifyEvent e) {
					Parameter param = currentParameters.get(index);
					param.setName(newParameterTxt.getText());
					currentParameters.add(index, param);
			    }
			};

			newParameterTxt.addModifyListener(paramNameListener);

			newParameterValueTxt = new Text(formGroup, SWT.BORDER);
			newParameterValueTxt.setText(currentParameters.get(i).getValue());
			newParameterValueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			
			//Listener to detect changes in text
			ModifyListener paramValueListener = new ModifyListener() {
			    /** {@inheritDoc} */
			    public void modifyText(ModifyEvent e) {
					Parameter param = currentParameters.get(index);
					param.setValue(newParameterValueTxt.getText());
					currentParameters.add(index, param);
			    }
			};

			newParameterValueTxt.addModifyListener(paramValueListener);
			
			label = new Label(formGroup, SWT.NONE);
			label.setText("");
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
			
			//paramListLbl.setText(paramListLbl.getText() + ", " + currentParameters.get(i).getName() + " " + currentParameters.get(i).getType() + " "
			//		+ currentParameters.get(i).getValue());
		}

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Define available asserts

		// Create a dropdown Combo & Read only
		assertionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		assertionsCb.setItems(Constants.assertions);
		//assertionsCb.select(0);
		
		String assertion = testData.getAssertion();
		if ( assertion.equals("isNull")){
			assertionsCb.select(0);
		}		
		else if(assertion.equals("isNotNull"))
		{
			assertionsCb.select(1);
		}
		else if(assertion.equals("isTrue"))
		{
			assertionsCb.select(2);
		}
		else if(assertion.equals("isFalse"))
		{
			assertionsCb.select(3);
		}
		else if(assertion.equals("areEqual"))
		{
			assertionsCb.select(4);
		}
		else if(assertion.equals("areNotEqual"))
		{
			assertionsCb.select(5);
		}

		
		
		assertionsCb.pack();

		// Create a dropdown Combo & Read only
		expectedTypeCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		expectedTypeCb.setItems(Constants.types);
		expectedTypeCb.select(0);
		expectedTypeCb.setVisible(false);

		// User select a item in the Combo.
		expectedTypeCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = expectedTypeCb.getSelectionIndex();
				selectedExpectedType = expectedTypeCb.getItem(idx);
			}
		});

		// User select a item in the Combo.
		assertionsCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = assertionsCb.getSelectionIndex();
				String selected = assertionsCb.getItem(idx);
				if (selected.equals("isNull") || selected.equals("isTrue") || selected.equals("isFalse")) {
					selectedAssertion = selected;
					expectedTypeCb.setVisible(false);
					expectedTxt.setVisible(false);
					expectedTxt.setText("");
				} else {
					selectedAssertion = selected;
					expectedTypeCb.setVisible(true);
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


		//paramListLbl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 5, 1));

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

		// Cancels the update
		cancelBtn = new Button(formGroup, SWT.PUSH);
		cancelBtn.setText("Cancel");
		cancelBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cancelBtn.addListener(SWT.Selection, event -> cancel());

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

	/**
	 * Submits edited data of the unit test
	 *
	 * @param
	 * @return
	 */
	private void save(Shell parent) {
		Expected exp = new Expected(selectedExpectedType, expectedTxt.getText());
		testData.setExpected(exp);
		testData.setAssertion(selectedAssertion);
		testData.setParameters(currentParameters);
		testData.setTestName(nameTxt.getText());
		MessageBox dialog = new MessageBox(parent, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Edition confirmation");
		dialog.setMessage("Are you sure you want to submit changes to unit test?");

		// open dialog and await user selection
		int returnCode = dialog.open();

		if (returnCode == 32) {
			try {
				MessageBox msg = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
				msg.setText("Edition confirmation");
				msg.setMessage("Unit test was edited sucessfully");
				msg.open();

			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void cancel() {
		nameTxt.setText(testData.getTestName());
		// parametersTxt.setText("");
		// expectedTxt.setText("");
	}

	private void addParameter(String name, String type, String value) {
		Parameter newParam = new Parameter();
		newParam.setName(name);
		newParam.setType(type);
		newParam.setValue(value);
		testData.getParameters().add(newParam);

		newParameterTxt.setText("");
		selectedNewParamType = "";
		newParameterValueTxt.setText("");

		paramListLbl.setText(paramListLbl.getText() + ", " + newParam.getName() + " " + newParam.getType() + " "
				+ newParam.getValue());

		makeBold(paramListLbl);
		paramListLbl.pack();
	}

}
