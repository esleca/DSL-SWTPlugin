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

import com.dsl.models.dtos.ClassFunctionsRequest;
import com.dsl.models.dtos.ClassFunctionsResponse;
import com.dsl.models.dtos.UnitTestResponse;
import com.dsl.models.parameters.ParameterFunction;
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
	private Button saveBtn, cleanBtn;
	private Label label, paramListLbl, expTypeLabel, expValLabel, expectedType;
	private Text nameTxt, expectedTxt;
	private Composite layer;
	private String selectedAssertion, selectedExpectedType, path, selectedFunction;
	private UnitTestRequest data = new UnitTestRequest();
	private Combo expectedTypeCb, assertionsCb;
	private List<ClassFunctionsResponse> functionInfoList;
	private String[] functionList;

	public CreationWindow() {

	}

	public CreationWindow(String path) {
		this.path = path;
		this.functionList = getFunctions();
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

		new GsonBuilder().setPrettyPrinting().create();

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


		// ------------------------ start dynamic parameters ---------------------

		Composite form = new Composite(center, SWT.NONE);

		Composite submit = new Composite(center, SWT.NONE);  
		

		GridData formData = new GridData(SWT.FILL, SWT.FILL, true, true);
		form.setLayoutData(formData);

		GridData submitData = new GridData(SWT.FILL, SWT.FILL, true, false);
		submit.setLayoutData(submitData);

		GridLayout submitLayout = new GridLayout(3, true);
		submit.setLayout(submitLayout);
		// ------------------------ end dynamic parameters -----------------------

		// Form to define the parameters of the unit test
		formGroup = new Group(form, SWT.FILL);
		formGroup.setText("New unit test");
		GridLayout formLayout = new GridLayout(3, true);
		formLayout.horizontalSpacing = 10;
		formLayout.marginTop = 5;
		formLayout.marginBottom = 0;
		formGroup.setLayout(formLayout);
		formGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		label = new Label(formGroup, SWT.NONE);
		label.setText("Class name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		makeBold(label);

		label = new Label(formGroup, SWT.NONE);
		label.setText(getClassName());
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));

		label = new Label(formGroup, SWT.NONE);
		label.setText("Function name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);



		// Create a dropdown Combo & Read only
		Combo functionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		functionsCb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));

		// Define available functions for tests
		functionsCb.setItems(functionList);
		//functionsCb.select(0);

		// User select a item in the Combo.
		functionsCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = functionsCb.getSelectionIndex();
				selectedFunction = functionsCb.getItem(idx);
				loadParameters(formGroup);
			}
		});
		

		label = new Label(formGroup, SWT.NONE);
		label.setText("Test name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		nameTxt = new Text(formGroup, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		nameTxt.setText(getUTName());
		
		label = new Label(formGroup, SWT.NONE);
		label.setText("Expected assert:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(label);

		// Create a dropdown Combo & Read only
		assertionsCb = new Combo(formGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		assertionsCb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		// Define available asserts
		assertionsCb.setItems(Constants.assertions);
		assertionsCb.setText("Assert");

		
		//------------------------------------------------------------------------------------------------------- 
		
		expTypeLabel = new Label(formGroup, SWT.NONE);
		expTypeLabel.setText("Expected type:");
		expTypeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(expTypeLabel);
		expTypeLabel.setVisible(false);
		
		// Create a dropdown Combo & Read only
		expectedType = new Label(formGroup, SWT.NONE);
		expectedType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		expectedType.setText(data.getExpected().getType());
		expectedType.setVisible(false);


		// User select a item in the Combo.
		assertionsCb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = assertionsCb.getSelectionIndex();
				String selected = assertionsCb.getItem(idx);
				if (selected.equals("isNull") || selected.equals("isNotNull") || selected.equals("isTrue") || selected.equals("isFalse")) {
					selectedAssertion = selected;
					expectedType.setVisible(false);
					expectedTxt.setVisible(false);
					expectedTxt.setText("");
					selectedExpectedType = "null";
					//objeto expected nulo
					expectedTxt.setText("false");
					expTypeLabel.setVisible(false);
					expValLabel.setVisible(false);
				} else {
					selectedAssertion = selected;
					expectedType.setVisible(true);
					expectedTxt.setVisible(true);
					expectedTxt.setText("");
					expTypeLabel.setVisible(true);
					expValLabel.setVisible(true);
				}
			}
		});
		
		
		expValLabel = new Label(formGroup, SWT.NONE);
		expValLabel.setText("Expected value:");
		expValLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		makeBold(expValLabel);
		expValLabel.setVisible(false);

		expectedTxt = new Text(formGroup, SWT.BORDER);
		expectedTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		expectedTxt.setVisible(false);


		// ------------------------------------------------------------------------------------------------------------------------

		// Saves the unit test information
		saveBtn = new Button(submit, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		saveBtn.setSize(92, 29);
		saveBtn.addListener(SWT.Selection, event -> save(layer.getShell()));

		label = new Label(submit, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

		// Clears the data of the form
		cleanBtn = new Button(submit, SWT.PUSH);
		cleanBtn.setText("Clean");
		cleanBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cleanBtn.addListener(SWT.Selection, event -> clear());

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

	private void save(Shell parent) {
		if (selectedFunction == null || selectedFunction == "") {
			MessageBox msgBox = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
			msgBox.setText("Missing information");
			msgBox.setMessage("Select function name");
			msgBox.open();
		} else {
			data.setClassPath(path);
			data.setClassName(getClassName());
			data.setFunctionName(selectedFunction);
			data.setTestName(nameTxt.getText());
			data.setOutputPath("C:\\TestPrinter\\JAVA");
			data.getExpected().setValue(expectedTxt.getText());
			data.setAssertion(selectedAssertion);

			MessageBox dialog = new MessageBox(parent, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			dialog.setText("Creation confirmation");
			dialog.setMessage("Are you sure you want to submit unit test?");

			// open dialog and await user selection
			int returnCode = dialog.open();

			if (returnCode == 32) {
				try {

					Message msg = new Message("CREATE", new Gson().toJson(data).toString(), "");

					Socket socket = new Socket(Constants.hostName, Constants.portNumber);

					DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
					dout.writeUTF(new Gson().toJson(msg).toString());
					dout.flush();

					socket.close();

					MessageBox msgBox = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
					msgBox.setText("Creation confirmation");
					msgBox.setMessage("New unit test was created successfully");
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
		return "";
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
			functionInfoList = new Gson().fromJson(din.readUTF(), listType);
			socket.close();

			String[] functions = new String[functionInfoList.size()];
			for (int i = 0; i < functionInfoList.size(); i++) {
				functions[i] = functionInfoList.get(i).getName();
				data.setExpected(new Expected(functionInfoList.get(i).getReturn(), ""));
				System.out.print( functionInfoList.get(i).getReturn());
			}
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

	private void loadParameters(Composite group) {
		try {
			label = new Label(group, SWT.NONE);
			label.setText("Function parameters: ");
			makeBold(label);
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 3, 1));
			for (int i = 0; i < functionInfoList.size(); i++) {
				if (functionInfoList.get(i).getName().equals(selectedFunction)) {
					ArrayList<ParameterFunction> parameterList = functionInfoList.get(0).getParameters();
					for(int k=0; k < parameterList.size(); k++) {
						
						final int index = k;
						ParameterFunction param = parameterList.get(k);
						label = new Label(group, SWT.NONE);
						label.setText(param.getType() + ' ' + param.getName());
						label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

						
						Parameter newParam = new Parameter();
						newParam.setName(param.getName());
						newParam.setType(param.getType());
						data.getParameters().add(newParam);
						
						Text valueTxt = new Text(group, SWT.BORDER);
						valueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
						valueTxt.setText("");
						valueTxt.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								
								Text txt = (Text) e.widget;
								System.out.println(txt.getText());
								
								newParam.setValue(txt.getText());
								data.getParameters().set(index, newParam);

							}
						});
						
					}
					group.pack();
					break;
				}

			}

		} catch (Exception e) {
			System.out.print("could not load the parameters");
		}
	}

}
