package com.tec.dslunittests.views;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.dsl.models.dtos.ClassTestsRequest;
import com.dsl.models.dtos.UnitTestResponse;
import com.dsl.models.unittests.TestScenario;
import com.dsl.models.unittests.UnitTest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tec.dslunittests.models.Expected;
import com.tec.dslunittests.models.Message;
import com.tec.dslunittests.models.Parameter;
import com.tec.dslunittests.models.UnitTestRequest;
import com.tec.dslunittests.resources.Constants;

public class ClassScopeWindow {

	private Button newBtn, refreshBtn;
	private Composite layer;
	private CTabFolder folder;
	private String path;
	private List<UnitTestResponse> list = null;
	private Socket socket;
	private String className, packageName;

	public ClassScopeWindow() {

	}

	public ClassScopeWindow(String path) {
		this.path = path;
		this.className = getClassName(this.path);
		try {
			this.packageName = getPackageName(this.path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Defines widgets and layout for the package unit test window
	 *
	 * @param parent main composite in which the view must be rendered
	 * @return the composite ready to be displayed
	 * @throws IOException file cannot be read
	 */
	public Composite render(Composite parent) {
		// Creates new composite layer to add widgets
		layer = new Composite(parent, SWT.NONE);
		// layer.setLayout(new FillLayout(SWT.VERTICAL));
		layer.setLayout(new GridLayout(1, false));

		// Creates new composite layer to add widgets
		Composite topLayer = new Composite(layer, SWT.NONE);
		topLayer.setLayout(new GridLayout(3, false));

		Composite bottomLayer = new Composite(layer, SWT.NONE);
		bottomLayer.setLayout(new FillLayout(SWT.VERTICAL));

		GridData topData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData bottomData = new GridData(SWT.FILL, SWT.FILL, true, true);

		topLayer.setLayoutData(topData);
		bottomLayer.setLayoutData(bottomData);

		// Columns to divide the view
		Composite left = new Composite(topLayer, SWT.NONE);
		Composite center = new Composite(topLayer, SWT.NONE);
		Composite right = new Composite(topLayer, SWT.NONE);

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

			topData.heightHint = (int) (size.y * 0.15);
			bottomData.heightHint = (int) (size.y - topData.heightHint);

			leftData.widthHint = (int) (size.x * 0.25);
			rightData.widthHint = (int) (size.x * 0.25);
			centerData.widthHint = size.x - leftData.widthHint - rightData.widthHint;

		});
		
		layer.pack();
		// Saves the unit test information
		refreshBtn = new Button(left, SWT.PUSH);
		refreshBtn.setText("Refresh");
		refreshBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
	

		// Saves the unit test information
		newBtn = new Button(left, SWT.PUSH);
		newBtn.setText("New unit test");
		newBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		newBtn.addListener(SWT.Selection, event -> loadCreationWindow(parent));

		Gson gson = new Gson();
		
		Table table = new Table(bottomLayer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		
		loadTestList(bottomLayer, parent, table);
		
		refreshBtn.addListener(SWT.Selection, event -> {
			loadTestList(bottomLayer, parent, table);
			});
		

		return layer;
	}

	/**
	 * Loads the view for editing the unit test
	 *
	 * @param parent main composite in which the view must be rendered and data of
	 *               the unit test
	 * @return
	 */
	private void loadEditionWindow(Composite parent, UnitTestRequest editable) {

		folder = (CTabFolder) parent;

		int idx = folder.getSelectionIndex();

		// Creates new tab item for content of the window
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("Edit unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		EditionWindow editionWindow = new EditionWindow();

		// Renders content in new tab
		item.setControl(editionWindow.render(parent, editable));

		// Refreshes view
		parent.requestLayout();
	}

	/**
	 * Shows confirmation message and if accepted, deletes unit test
	 *
	 * @param shell main composite in which the view must be rendered and data of
	 *              the unit test
	 * @return
	 */
	private void deleteUnitTest(Shell shell, UnitTestRequest editable) {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Delete confirmation");
		dialog.setMessage("Are you sure you want to delete \"" + editable.getTestName() + "\" unit test");

		// open dialog and await user selection
		int returnCode = dialog.open();

		if (returnCode == 32) {
			// Here goes the code to delete unit test
			Message msg = new Message("DELETE", new Gson().toJson(editable).toString(), "");
			try {
				socket = new Socket(Constants.hostName, Constants.portNumber);
				DataInputStream din = new DataInputStream(socket.getInputStream());
				DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

				dout.writeUTF(new Gson().toJson(msg).toString());
				dout.flush();
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Shows confirmation message and if accepted, deletes unit test
	 *
	 * @param parent composite in which the view must be rendered and data of the
	 *               unit test
	 * @return
	 */
	private void loadCreationWindow(Composite parent) {
		folder = (CTabFolder) parent;

		int idx = folder.getSelectionIndex();

		// Creates new tab to display content
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("New unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		CreationWindow creationWindow = new CreationWindow(path);
		// Renders content in new tab
		item.setControl(creationWindow.render(parent));

		// Refreshes view
		parent.requestLayout();
	}

	private List<UnitTestResponse> getClassUnitTests(String packageName, String className)
			throws UnknownHostException, IOException {
		// Writing request information to json
		ClassTestsRequest listRequest = new ClassTestsRequest(packageName, className);
		Message msg = new Message("LIST", new Gson().toJson(listRequest).toString(), "CLASS");

		socket = new Socket(Constants.hostName, Constants.portNumber);

		DataInputStream din = new DataInputStream(socket.getInputStream());
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

		dout.writeUTF(new Gson().toJson(msg).toString());
		dout.flush();

		Type listType = new TypeToken<ArrayList<UnitTestResponse>>() {
		}.getType();
		List<UnitTestResponse> list = new Gson().fromJson(din.readUTF(), listType);
		socket.close();
		return list;
	}

	
	private void loadTestList(Composite layer, Composite parent, Table table) {
		
		table.removeAll();

		String[] columns = { "#", "Function", "Unit test", "Parameters", "Assertion", " ", " " };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columns[i]);
			table.getColumn(i).pack();
		}

		try {

			list = getClassUnitTests(this.packageName, this.className);
			System.out.println("package: " + this.packageName);
			System.out.println("class: " + this.className);

			for (int i = 0; i <= list.size() - 1; i++) {
				UnitTestResponse responseItem = list.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, "" + (i + 1));
				item.setText(1, responseItem.getClassName());
				item.setText(2, responseItem.getTestName());

				// Data of the existent unit tests
				UnitTestRequest editable = new UnitTestRequest();
				editable.setTestName(responseItem.getTestName());
				editable.setAssertion(responseItem.getAssertion());
				editable.setClassName(responseItem.getClassName());
				editable.setClassPath("");
				editable.setFunctionName(responseItem.getFunctionName());

				List<Parameter> params = new ArrayList<Parameter>();

				String parameters = "";
				for (int k = 0; k <= responseItem.getParameters().size() - 1; k++) {
					parameters += responseItem.getParameters().get(k).getName() + " - ";
					params.add(new Parameter(responseItem.getParameters().get(k).getName(),
							responseItem.getParameters().get(k).getType(),
							responseItem.getParamValues().get(k).toString()));
				}
				item.setText(3, parameters);
				editable.setParameters(params);

				String expected = responseItem.getAssertion();
				item.setText(4, expected);

				Expected exp = new Expected(responseItem.getExpectedType(), responseItem.getExpectedValue().toString());
				editable.setExpected(exp);

				// Set attributes for the button to load edition window
				Button editBtn = new Button(table, SWT.PUSH);
				editBtn.setText("Edit");
				editBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
				editBtn.addListener(SWT.Selection, event -> loadEditionWindow(parent, editable));

				TableEditor editor = new TableEditor(table);
				editor.setEditor(editBtn, item, 5);

				// Set attributes of the editor
				editor.grabHorizontal = true;
				editor.minimumHeight = editBtn.getSize().y;
				editor.minimumWidth = editBtn.getSize().x;

				// Set attributes for the delete button
				Button deleteBtn = new Button(table, SWT.PUSH);
				deleteBtn.setText("Delete");
				deleteBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
				deleteBtn.addListener(SWT.Selection, event -> deleteUnitTest(parent.getShell(), editable));

				editor = new TableEditor(table);
				editor.setEditor(deleteBtn, item, 6);

				// Set attributes of the editor
				editor.grabHorizontal = true;
				editor.minimumHeight = deleteBtn.getSize().y;
				editor.minimumWidth = deleteBtn.getSize().x;
			}
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < columns.length; i++) {
			table.getColumn(i).pack();
		}
		
		// Refreshes view
		layer.requestLayout();
	}
	
	private String getClassName(String path) {
		File file = new File(path);
		String str = file.getName();
		return str.substring(0, str.lastIndexOf('.'));
	}
	
	private String getPackageName(String path) throws FileNotFoundException {
		File file = new File(path);
		Scanner scanner = new Scanner(file);
		String currentLine = null;

		while(scanner.hasNext())
		{
			currentLine = scanner.next();
		    if(currentLine.indexOf("package") == 0)
		    {
		    	String next = scanner.next();
		    	String[] arrOfStr = next.split(";", 2);
		    	return arrOfStr[0];
		    }
		}
		return currentLine;
	}

}
