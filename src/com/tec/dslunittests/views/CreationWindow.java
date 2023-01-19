package com.tec.dslunittests.views;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

import com.dsl.models.dtos.ClassFunctionsRequest;
import com.dsl.models.dtos.ClassFunctionsResponse;
import com.dsl.models.dtos.UnitTestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.tec.dslunittests.models.Expected;
import com.tec.dslunittests.models.Message;
import com.tec.dslunittests.models.Parameter;
import com.tec.dslunittests.models.UnitTestRequest;
import com.tec.dslunittests.resources.Constants;

public class CreationWindow {

	private Group formGroup;
	private Button saveBtn, cleanBtn, addBtn;
	private Label label, paramListLbl;
	private Text nameTxt, functionTxt, expectedTxt, newParameterTxt, newParameterValueTxt, assertionTxt;
	private Composite layer;
	private String selectedAssertion, selectedExpectedType, selectedNewParamType, path, selectedFunction;
	private Gson gson;
	private UnitTestRequest data = new UnitTestRequest();
	private Combo expectedTypeCb, assertionsCb;

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

		// Create a dropdown Combo & Read only
		Combo functionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		functionsCb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));

		// Define available data types
		String[] functions = getFunctions();
		functionsCb.setItems(functions);
		functionsCb.select(0);

		// User select a item in the Combo.
		functionsCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = functionsCb.getSelectionIndex();
				selectedFunction = functionsCb.getItem(idx);
			}
		});

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
		label.setText("Parameters:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Create a dropdown Combo & Read only
		Combo typesCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available data types
		String[] types = new String[] { "int", "String", "boolean", "char", "double", "float", "long" };
		typesCb.setItems(types);
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
				event -> addParameter(newParameterTxt.getText(), selectedNewParamType, newParameterValueTxt.getText(), layer.getShell()));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Create a dropdown Combo & Read only
		assertionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available asserts
		String[] assertions = new String[] { "isNull", "isTrue", "isFalse", "areEqual", "areNotEqual",
				"isInstanceOfType" };
		assertionsCb.setItems(assertions);
		assertionsCb.setText("Assert");

		// Create a dropdown Combo & Read only
		expectedTypeCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);

		// Define available asserts
		expectedTypeCb.setItems(types);
		expectedTypeCb.setText("Data type");
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
					selectedExpectedType = "boolean";
					expectedTxt.setText("false");
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
		expectedTxt.setMessage("Expected value");
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
		if(selectedFunction == null || selectedFunction == "") {
			MessageBox msgBox = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
			msgBox.setText("Missing information");
			msgBox.setMessage("Select function name");
			msgBox.open();
		}else {
			data.setClassPath(path);
			data.setClassName(getClassName());
			data.setFunctionName(selectedFunction);
			data.setTestName(nameTxt.getText());
			data.setOutputPath("C:\\TestPrinter\\JAVA");
			Expected exp = new Expected(selectedExpectedType, expectedTxt.getText());
			data.setExpected(exp);
			data.setAssertion(selectedAssertion);

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

					Message msg = new Message("CREATE", new Gson().toJson(data).toString(), "");

					Socket socket = new Socket(Constants.hostName, Constants.portNumber);

					DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
					dout.writeUTF(new Gson().toJson(msg).toString());
					dout.flush();

					socket.close();

					MessageBox msgBox = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
					msgBox.setText("Creation confirmation");
					msgBox.setMessage("New unit test was created sucessfully");
					msgBox.open();
					clear();

				} catch (JsonIOException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private void clear() {
		nameTxt.setText(getUTName());
		paramListLbl.setText("");
		expectedTxt.setText("");
		expectedTxt.setVisible(false);
		expectedTxt.setText("");
		expectedTypeCb.setVisible(false);
		data = new UnitTestRequest();
	}

	private String getUTName() {
		return "test_name";
	}

	private void addParameter(String name, String type, String value, Shell parent) {
		if(type == "" || selectedNewParamType == null || type == null) {
			MessageBox msgBox = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
			msgBox.setText("Missing information");
			msgBox.setMessage("Select parameter type");
			msgBox.open();
		}else {
		Parameter newParam = new Parameter();
		newParam.setName(name);
		newParam.setType(type);
		newParam.setValue(value);
		data.getParameters().add(newParam);

		newParameterTxt.setText("");
		selectedNewParamType = "";
		newParameterValueTxt.setText("");

		paramListLbl.setText(paramListLbl.getText() + ", " + name + " " + type + " " + value);

		makeBold(paramListLbl);
		paramListLbl.pack();
		}
	}

	private String getClassName() {
		File file = new File(path);
		String str = file.getName();
		return str.substring(0, str.lastIndexOf('.'));
	}

	private String[] getFunctions() {
		ClassFunctionsRequest classRequest = new ClassFunctionsRequest(path, "JAVA");
		Message msg = new Message("LIST", new Gson().toJson(classRequest).toString(), "FUNCTION");
		try {
			Socket socket = new Socket(Constants.hostName, Constants.portNumber);
			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

			dout.writeUTF(new Gson().toJson(msg).toString());
			dout.flush();
			Type listType = new TypeToken<ArrayList<ClassFunctionsResponse>>() {
			}.getType();
			List<ClassFunctionsResponse> list = new Gson().fromJson(din.readUTF(), listType);
			socket.close();
			
			String[] functions = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				functions[i] = list.get(i).getName();
			}
			System.out.print(list.get(0).getName());
			return functions;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
