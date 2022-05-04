package com.tec.dslunittests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

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
