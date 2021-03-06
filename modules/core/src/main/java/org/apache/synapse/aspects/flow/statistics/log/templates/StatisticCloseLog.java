/*
 *   Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.aspects.flow.statistics.log.templates;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.aspects.flow.statistics.collectors.RuntimeStatisticCollector;
import org.apache.synapse.aspects.flow.statistics.data.raw.StatisticDataUnit;
import org.apache.synapse.aspects.flow.statistics.log.StatisticReportingLog;

public class StatisticCloseLog implements StatisticReportingLog {

	private StatisticDataUnit statisticDataUnit;

	public StatisticCloseLog(MessageContext messageContext, String componentId, String parentId, Long endTime) {
		String statisticId = (String) messageContext.getProperty(SynapseConstants.FLOW_STATISTICS_ID);
		int msgId;
		if (messageContext.getProperty(SynapseConstants.FLOW_STATISTICS_MESSAGE_ID) != null) {
			msgId = (Integer) messageContext.getProperty(SynapseConstants.FLOW_STATISTICS_MESSAGE_ID);
		} else {
			msgId = 0;
		}

		statisticDataUnit =
				new StatisticDataUnit(statisticId, componentId, parentId, msgId, endTime, messageContext.isResponse(),
				                      messageContext.getEnvironment());
	}

	public StatisticCloseLog(MessageContext messageContext, String componentId, String parentId, Long endTime,
	                         boolean isCloneLog, boolean isAggregateLog) {
		this(messageContext, componentId, parentId, endTime);
		if (isAggregateLog) {
			statisticDataUnit.setAggregatePoint();
		}

		if (isCloneLog) {
			statisticDataUnit.setClonePoint();
		}
	}

	@Override public void process() {
		RuntimeStatisticCollector.recordStatisticCloseLog(statisticDataUnit);
	}
}
