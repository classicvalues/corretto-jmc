/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The contents of this file are subject to the terms of either the Universal Permissive License
 * v 1.0 as shown at http://oss.oracle.com/licenses/upl
 *
 * or the following license:
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openjdk.jmc.rjmx.ui.internal;

import java.lang.reflect.Array;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuManager;

import org.openjdk.jmc.rjmx.services.IAttribute;
import org.openjdk.jmc.rjmx.services.IIndexedAttributeChild;
import org.openjdk.jmc.rjmx.ui.messages.internal.Messages;
import org.openjdk.jmc.ui.column.ColumnManager;
import org.openjdk.jmc.ui.column.IColumn;

public class InsertArrayElementMenuAction extends AbstractArrayElementMenuAction {

	private final boolean m_insertBelow;

	protected InsertArrayElementMenuAction(IMenuManager mm, ColumnManager columnsManager, IColumn column,
			boolean insertBelow) {
		super(insertBelow ? Messages.INSERT_ARRAY_ELEMENT_BELOW_ACTION : Messages.INSERT_ARRAY_ELEMENT_ABOVE_ACTION, mm,
				columnsManager, column);
		m_insertBelow = insertBelow;
	}

	@Override
	protected void run(IIndexedAttributeChild selectedElement) {
		IAttribute parent = (IAttribute) selectedElement.getParent();
		Object array = parent.getValue();
		Object newArray = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array) + 1);
		int selectedIndex = selectedElement.getIndex() + ((m_insertBelow) ? 1 : 0);
		if (selectedIndex > 0) {
			System.arraycopy(array, 0, newArray, 0, selectedIndex);
		}
		if (selectedIndex <= Array.getLength(array) - 1) {
			System.arraycopy(array, selectedIndex, newArray, selectedIndex + 1, Array.getLength(array) - selectedIndex);
		}
		parent.setValue(newArray);
	}

	public static ActionContributionItem createInsertArrayElementMenuActionContribution(
		IMenuManager mm, ColumnManager columnsManager, IColumn column, boolean insertBelow) {
		return new InsertArrayElementMenuAction(mm, columnsManager, column, insertBelow).getActionContribution();
	}
}
