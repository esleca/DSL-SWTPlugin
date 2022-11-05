package com.tec.dslunittests.views;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
import com.dsl.models.dtos.PackageTestsRequest;
import com.dsl.models.dtos.UnitTestResponse;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.tec.dslunittests.models.Message;
import com.tec.dslunittests.models.Parameter;
import com.tec.dslunittests.models.UnitTestRequest;
import com.tec.dslunittests.resources.Constants;

public class PackageScopeWindow {

	private Button editBtn, deleteBtn, newBtn;
	private Composite layer;
	private CTabFolder folder;
	private Socket socket;
	private String path;
	private List<UnitTestResponse> list = null;
	private String jsonString = """
			[

				{
					"packageName": "package_1",
					"className": "class1",
					"function": "getEdades",
					"testName": "getEdades_test1",
					"assertion":"AreEqual",
					"expected": {
						"type": "int",
						"value": "14"
					},
					"parameters": [
						{
			                "name": "num1",
			                "type": "int",
			                "value": "9"
			            },
						{
			                "name": "num2",
			                "type": "int",
			                "value": "2"
			            },
						{
			                "name": "num3",
			                "type": "int",
			                "value": "3"
			            }
					]
				}, {
					"packageName": "package_1",
					"className": "class1",
					"function": "getEdades",
					"testName": "getEdades_test1",
					"assertion":"AreEqual",
					"expected": {
						"type": "int",
						"value": "14"
					},
					"parameters": [
						{
			                "name": "num1",
			                "type": "int",
			                "value": "9"
			            },
						{
			                "name": "num2",
			                "type": "int",
			                "value": "2"
			            },
						{
			                "name": "num3",
			                "type": "int",
			                "value": "3"
			            }
					]
				}, {
					"packageName": "package_1",
					"className": "class1",
					"function": "getEdades",
					"testName": "getEdades_test1",
					"assertion":"AreEqual",
					"expected": {
						"type": "int",
						"value": "14"
					},
					"parameters": [
						{
			                "name": "num1",
			                "type": "int",
			                "value": "9"
			            },
						{
			                "name": "num2",
			                "type": "int",
			                "value": "2"
			            },
						{
			                "name": "num3",
			                "type": "int",
			                "value": "3"
			            }
					]
				}
			]""";

	public PackageScopeWindow(String path) {
		this.path = path;
	}

	public PackageScopeWindow() {

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

		// Saves the unit test information
		newBtn = new Button(right, SWT.PUSH);
		newBtn.setText("New unit test");
		newBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		newBtn.addListener(SWT.Selection, event -> loadCreationWindow(parent));

		Gson gson = new Gson();

		// Transforms json data to java object
		UnitTestRequest[] UTdata = gson.fromJson(jsonString, UnitTestRequest[].class);

		Table table = new Table(bottomLayer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		String[] columns = { "#", "Class", "Function", "Unit test", "", "" };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columns[i]);
			table.getColumn(i).pack();
		}

		try {
			// Writing request information to json
			PackageTestsRequest listRequest = new PackageTestsRequest("com.sample.demo");
			Message msg = new Message("LIST", new Gson().toJson(listRequest).toString(), "PACKAGE");

			socket = new Socket(Constants.hostName, Constants.portNumber);

			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

			dout.writeUTF(new Gson().toJson(msg).toString());
			dout.flush();

			Type listType = new TypeToken<ArrayList<UnitTestResponse>>() {
			}.getType();
			list = new Gson().fromJson(din.readUTF(), listType);

			socket.close();

			for (int i = 0; i <= list.size() - 1; i++) {
				UnitTestResponse responseItem = list.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, "" + (i + 1));
				item.setText(1, responseItem.getClassName());
				item.setText(2, responseItem.getFunctionName());
				item.setText(3, responseItem.getTestName());

				// Data of the existent unit tests
				UnitTestRequest editable = new UnitTestRequest();
				editable.setTestName(responseItem.getTestName());
				editable.setAssertion(responseItem.getAssertion());
				editable.setClassName(responseItem.getClassName());
				editable.setClassPath("");
				editable.setFunctionName(responseItem.getFunctionName());
				
				List<Parameter> params = new ArrayList<Parameter>();

				for (int k = 0; k <= responseItem.getParameters().size() - 1; k++) {
					params.add(new Parameter(responseItem.getParameters().get(k).getName(), responseItem.getParameters().get(k).getType(), responseItem.getParamValues().get(k)));
				}
				editable.setParameters(params);

				// Set attributes for the button to load edition window
				editBtn = new Button(table, SWT.PUSH);
				editBtn.setText("Edit");
				editBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
				editBtn.addListener(SWT.Selection, event -> loadEditionWindow(parent, editable));

				TableEditor editor = new TableEditor(table);
				editor.setEditor(editBtn, item, 4);

				// Set attributes of the editor
				editor.grabHorizontal = true;
				editor.minimumHeight = editBtn.getSize().y;
				editor.minimumWidth = editBtn.getSize().x;

				// Set attributes for the delete button
				deleteBtn = new Button(table, SWT.PUSH);
				deleteBtn.setText("Delete");
				deleteBtn.computeSize(SWT.DEFAULT, table.getItemHeight());
				deleteBtn.addListener(SWT.Selection, event -> deleteUnitTest(parent.getShell(), editable));

				editor = new TableEditor(table);
				editor.setEditor(deleteBtn, item, 5);

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

		topLayer.pack();
		bottomLayer.pack();

		return layer;
	}

	/**
	 * Loads the view for editing the unit test
	 *
	 * @param parent main composite in which the view must be rendered and data of
	 *               the unit test
	 * @return
	 */
	private void loadEditionWindow(Composite parent, UnitTestRequest test) {

		folder = (CTabFolder) parent;

		int idx = folder.getSelectionIndex();

		// Creates new tab item for content of the window
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("Edit unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		EditionWindow editionWindow = new EditionWindow();

		// Renders content in new tab
		item.setControl(editionWindow.render(parent, test));

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
	private void deleteUnitTest(Shell shell, UnitTestRequest test) {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Delete confirmation");
		dialog.setMessage("Are you sure you want to delete \"" + test.getTestName() + "\" unit test");

		// open dialog and await user selection
		int returnCode = dialog.open();

		if (returnCode == 32) {
			// Here goes the code to delete unit test
			// Here goes the code to delete unit test
			Message msg = new Message("DELETE", new Gson().toJson(test).toString(), "");
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

	private void loadCreationWindow(Composite parent) {
		folder = (CTabFolder) parent;

		int idx = folder.getSelectionIndex();

		// Creates new tab to display content
		CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
		item.setText("New unit test");
		folder.setSelection(idx + 1);

		// Creates window to create individual unit tests
		CreationWindow creationWindow = new CreationWindow();
		// Renders content in new tab
		item.setControl(creationWindow.render(parent));

		// Refreshes view
		parent.requestLayout();
	}

}
