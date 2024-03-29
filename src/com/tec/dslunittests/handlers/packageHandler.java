package com.tec.dslunittests.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tec.dslunittests.UnitTests;
import com.tec.dslunittests.views.PackageScopeWindow;

public class packageHandler extends AbstractHandler {
	
	private IProject theProject;
	private IResource theResource;
	private IFile theFile;

	private String workspaceName;
	private String projectName;
	private String fileName;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		//Code retrieved from https://stackoverflow.com/a/11580733
				ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
				if (selection instanceof ITreeSelection) {
					System.out.println("Tree selection");
					TreeSelection treeSelection = (TreeSelection) selection;
					TreePath[] treePaths = treeSelection.getPaths();
					TreePath treePath = treePaths[0];

					// The TreePath contains a series of segments in our usage:
					// o The first segment is usually a project
					// o The last segment generally refers to the file

					// The first segment should be a IProject
					Object firstSegmentObj = treePath.getFirstSegment();
					this.theProject = (IProject) ((IAdaptable) firstSegmentObj).getAdapter(IProject.class);
					if (this.theProject == null) {
						System.out.println("Null project");
					}

					// The last segment should be an IResource
					Object lastSegmentObj = treePath.getLastSegment();
					this.theResource = (IResource) ((IAdaptable) lastSegmentObj).getAdapter(IResource.class);
					if (this.theResource == null) {
						System.out.println("Null resource");
					}

					// As the last segment is an IResource we should be able to get an IFile
					// reference from it
					this.theFile = (IFile) ((IAdaptable) lastSegmentObj).getAdapter(IFile.class);

					// Extract additional information from the IResource and IProject
					this.workspaceName = this.theResource.getWorkspace().getRoot().getLocation().toOSString();
					this.projectName = this.theProject.getName();
					this.fileName = this.theResource.getName();
					
					System.out.println(theResource.getFullPath());
					System.out.println(theResource.getLocation());
					System.out.println("Path: " + workspaceName + " " + projectName + " " + fileName);
					System.out.println(workspaceName +  theResource.getFullPath().toOSString());
				}

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
					item.setText("Package unit test");
					folder.setSelection(idx + 1);

					// Creates window to show package unit tests
					//PackageScopeWindow packageWindow = new PackageScopeWindow(workspaceName +  theResource.getFullPath().toOSString());
					PackageScopeWindow packageWindow = new PackageScopeWindow(theResource.getLocation().toString());
					// Renders content in new tab
					item.setControl(packageWindow.render(folder));

					// Refreshes view
					folder.requestLayout();

				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
	}

}
