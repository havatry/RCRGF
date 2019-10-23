/* ***** BEGIN LICENSE BLOCK *****
 * Copyright (C) 2010-2011, The VNREAL Project Team.
 * 
 * This work has been funded by the European FP7
 * Network of Excellence "Euro-NF" (grant agreement no. 216366)
 * through the Specific Joint Developments and Experiments Project
 * "Virtual Network Resource Embedding Algorithms" (VNREAL). 
 *
 * The VNREAL Project Team consists of members from:
 * - University of Wuerzburg, Germany
 * - Universitat Politecnica de Catalunya, Spain
 * - University of Passau, Germany
 * See the file AUTHORS for details and contact information.
 * 
 * This file is part of ALEVIN (ALgorithms for Embedding VIrtual Networks).
 *
 * ALEVIN is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License Version 3 or later
 * (the "GPL"), or the GNU Lesser General Public License Version 3 or later
 * (the "LGPL") as published by the Free Software Foundation.
 *
 * ALEVIN is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * or the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License and
 * GNU Lesser General Public License along with ALEVIN; see the file
 * COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package vnreal.ui.dialog;

import mulavito.algorithms.IAlgorithm;
import mulavito.ui.dialogs.ProgressBarDialog;
import vnreal.algorithms.GenericMappingAlgorithm;
import vnreal.ui.UI;

/**
 * An implementation of MuLaViTo's {@link ProgessBarDialog}.
 * <br>
 * 二阶段的流程
 * MyProgressBarDialog的构造函数 -> 调用super的构造函数,传入alg -> 实例化ProgressTask(alg)任务 -> 弹出框是否选择HiddenHop & 执行任务 
 * -> 调用任务的doInBackground方法 -> 执行AbstractAlgorithm的performEvaluation方法 -> 执行AbstractSequentialAlgorithm的evalute方法
 * -> 执行GenericMappingAlgorithm的process方法 -> 执行传入alg的nodemapping和linkmapping方法 -> 统计算法的计算结果，在onDone的时候显示
 * @author Michael Duelli
 * @since 2010-11-18
 */
@SuppressWarnings("serial")
public final class MyProgressBarDialog extends ProgressBarDialog {
	public MyProgressBarDialog(IAlgorithm algo) {
		super(UI.getInstance(), algo);
	}

	@Override
	protected void onUpdate() {
	}

	@Override
	protected void onBegin(IAlgorithm algo) {
		if (algo instanceof GenericMappingAlgorithm)
			new HiddenHopsDialog((GenericMappingAlgorithm) algo);
	}

	@Override
	protected void onDone(IAlgorithm arg0) {
		UI.getInstance().update();
	}
}
