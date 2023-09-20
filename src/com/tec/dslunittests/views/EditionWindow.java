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

import com.dsl.models.parameters.ParameterFunction;
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
	private Button saveBtn, cancelBtn;
	private Label label, expectedType, expTypeLabel, expValLabel;
	private Text nameTxt, expectedTxt;
	private Composite layer;
	private String selectedAssertion, selectedExpectedType;
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

		Composite form = new Composite(center, SWT.NONE);

		Composite submit = new Composite(center, SWT.NONE);

		GridData formData = new GridData(SWT.FILL, SWT.FILL, true, true);
		form.setLayoutData(formData);

		GridData submitData = new GridData(SWT.FILL, SWT.FILL, true, false);
		submit.setLayoutData(submitData);

		GridLayout submitLayout = new GridLayout(3, true);
		// submit.setLayoutData(dynamicData);
		submit.setLayout(submitLayout);

		// Form to define the parameters of the unit test
		formGroup = new Group(form, SWT.FILL);
		formGroup.setText("Edit unit test");
		GridLayout formLayout = new GridLayout(3, true);
		formLayout.horizontalSpacing = 10;
		formGroup.setLayout(formLayout);
		formGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Displays information about the location of the unit test
		label = new Label(formGroup, SWT.NONE);
		label.setText("Class name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText(testData.getClass().getName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Function name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText(testData.getFunctionName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Test name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		nameTxt.setText(testData.getTestName());

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected assert:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Define available asserts

		// Create a dropdown Combo & Read only
		assertionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		assertionsCb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		assertionsCb.setItems(Constants.assertions);
		// assertionsCb.select(0);

		String assertion = testData.getAssertion();

		if (assertion.equals("isNull")) {
			assertionsCb.select(0);
		} else if (assertion.equals("isNotNull")) {
			assertionsCb.select(1);
		} else if (assertion.equals("isTrue")) {
			assertionsCb.select(2);
		} else if (assertion.equals("isFalse")) {
			assertionsCb.select(3);
		} else if (assertion.equals("areEqual")) {
			assertionsCb.select(4);
		} else if (assertion.equals("areNotEqual")) {
			assertionsCb.select(5);
		}

		assertionsCb.pack();
		
		expTypeLabel = new Label(formGroup, SWT.NONE);
		expTypeLabel.setText("Expected type:");
		expTypeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(expTypeLabel);
		expTypeLabel.setVisible(false);

		// Create a dropdown Combo & Read only
		expectedType = new Label(formGroup, SWT.NONE);
		expectedType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		expectedType.setText(testData.getExpected().getType());
		expectedType.setVisible(false);

		expValLabel = new Label(formGroup, SWT.NONE);
		expValLabel.setText("Expected value:");
		expValLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(expValLabel);
		expValLabel.setVisible(false);

		// User select a item in the Combo.
		assertionsCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = assertionsCb.getSelectionIndex();
				String selected = assertionsCb.getItem(idx);
				if (selected.equals("isNull") || selected.equals("isNotNull") || selected.equals("isTrue")
						|| selected.equals("isFalse")) {
					selectedAssertion = selected;
					expectedTxt.setVisible(false);
					expectedType.setVisible(false);
					expTypeLabel.setVisible(false);
					expValLabel.setVisible(false);
				} else {
					selectedAssertion = selected;
					expectedTxt.setVisible(true);
					expectedType.setVisible(true);
					expTypeLabel.setVisible(true);
					expValLabel.setVisible(true);

				}
			}
		});

		expectedTxt = new Text(formGroup, SWT.BORDER);
		expectedTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		expectedTxt.setText(testData.getExpected().getValue());
		expectedTxt.setVisible(false);

		label = new Label(formGroup, SWT.NONE);
		label.setText("Function parameters: ");
		makeBold(label);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 3, 1));

		// Make all parameters editable
		currentParameters = testData.getParameters();
		// currentParameters = null;
		for (int i = 0; i < currentParameters.size(); i++) {

			final int index = i;
			Parameter param = currentParameters.get(i);
			label = new Label(formGroup, SWT.NONE);
			label.setText(param.getType() + ' ' + param.getName());
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

			Text valueTxt = new Text(formGroup, SWT.BORDER);
			valueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
			valueTxt.setText(param.getValue());
			valueTxt.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {

					Text txt = (Text) e.widget;
					System.out.println(txt.getText());

					param.setValue(txt.getText());
					testData.getParameters().set(index, param);

				}
			});

		}

		// Saves the unit test information
		saveBtn = new Button(submit, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		saveBtn.addListener(SWT.Selection, event -> save(layer.getShell()));

		label = new Label(submit, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		// Cancels the update
		cancelBtn = new Button(submit, SWT.PUSH);
		cancelBtn.setText("Cancel");
		cancelBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cancelBtn.addListener(SWT.Selection, event -> cancel());

		// Modifies dynamically the size of the columns on resize of the main window
		parent.addListener(SWT.Resize, arg0 -> {
			Point size = parent.getSize();

			leftData.widthHint = (int) (size.x * 0.05);
			rightData.widthHint = (int) (size.x * 0.05);
			centerData.widthHint = size.x - leftData.widthHint - rightData.widthHint;
			submitData.widthHint = size.x - leftData.widthHint - rightData.widthHint;

			formLayout.marginWidth = (int) (size.x * 0.015);
			submitLayout.marginWidth = (int) (size.x * 0.015);

			formData.heightHint = (int) (size.y * 0.9);
		});

		formGroup.pack();
		center.pack();
		submit.pack();

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
				msg.setMessage("Unit test was edited successfully");
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

}
