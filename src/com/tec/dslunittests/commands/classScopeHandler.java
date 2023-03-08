package com.tec.dslunittests.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tec.dslunittests.UnitTests;
import com.tec.dslunittests.views.ClassScopeWindow;
import com.tec.dslunittests.views.PackageScopeWindow;

public class classScopeHandler implements IHandler {
	
	private IProject theProject;
	private IResource theResource;
	private IFile theFile;

	private String workspaceName;
	private String projectName;
	private String fileName;

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		IPath path = ((IPathEditorInput)input).getPath();
		System.out.println(path.toOSString());

		try {
			//Retrieves or displays plugin view into workbench
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("com.tec.dslunittests.UnitTests");
			IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView("com.tec.dslunittests.UnitTests");

			UnitTests myView = (UnitTests) view;
			CTabFolder folder = myView.getFolder();

			int idx = folder.getSelectionIndex();

			// Creates new tab to display content
			CTabItem item = new CTabItem(folder, SWT.CLOSE, idx + 1);
			item.setText("Class unit test");
			folder.setSelection(idx + 1);

			// Creates window to show package unit tests
			ClassScopeWindow classWindow = new ClassScopeWindow(path.toOSString());
			// Renders content in new tab
			item.setControl(classWindow.render(folder));

			// Refreshes view
			folder.requestLayout();

		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
