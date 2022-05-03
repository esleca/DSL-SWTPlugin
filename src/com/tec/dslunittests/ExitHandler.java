package com.tec.dslunittests;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExitHandler implements IHandler {

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
		//System.out.println("....");
		//System.out.print(ISources.ACTIVE_MENU_SELECTION_NAME);
		//IStructuredSelection ss = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		//System.out.println(ss.getFirstElement());
		//HandlerUtil.getActiveWorkbenchWindow(event).close();
		
		 ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
	                .getActivePage().getSelection();
	        if (selection != null & selection instanceof IStructuredSelection) {
	            IStructuredSelection strucSelection = (IStructuredSelection) selection;
	            for (Iterator<Object> iterator = strucSelection.iterator(); iterator
	                    .hasNext();) {
	                Object element = iterator.next();
	                System.out.println(element.toString());
	            }
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
