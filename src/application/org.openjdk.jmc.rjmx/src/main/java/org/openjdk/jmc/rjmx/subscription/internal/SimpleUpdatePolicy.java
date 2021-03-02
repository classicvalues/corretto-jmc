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
package org.openjdk.jmc.rjmx.subscription.internal;

import org.openjdk.jmc.rjmx.subscription.IMRISubscription;
import org.openjdk.jmc.rjmx.subscription.IUpdatePolicy;

/**
 * Update policy that updates the {@link IMRISubscription} every {@link #getIntervalTime()}
 * millisecond.
 */
public final class SimpleUpdatePolicy extends AbstractUpdatePolicy implements IIntervalUpdatePolicy {
	private final static SimpleUpdatePolicy DEFAULT_POLICY = new SimpleUpdatePolicy(
			UpdatePolicyToolkit.getDefaultUpdateInterval());
	private final int updateTime;

	/**
	 * Creates an update policy with the specified update time.
	 *
	 * @param updateTime
	 *            the updateTime to use.
	 */
	private SimpleUpdatePolicy(int updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected long getFollowingUpdate(long lastUpdate) {
		int updateInterval = getIntervalTime();
		// Place the following update on a multiple of the update interval to synchronize retrieval
		return lastUpdate - lastUpdate % updateInterval + updateInterval;
	}

	@Override
	public int getIntervalTime() {
		return updateTime;
	}

	@Override
	protected long getFirstUpdate(long now) {
		return now;
	}

	/**
	 * Factory method for {@link SimpleUpdatePolicy}.
	 *
	 * @param updateTime
	 *            the update interval for this policy.
	 * @return the configured {@link SimpleUpdatePolicy}.
	 */
	public static IUpdatePolicy newPolicy(int updateTime) {
		if (DEFAULT_POLICY.updateTime == updateTime) {
			return DEFAULT_POLICY;
		} else if (updateTime == -1) {
			return DEFAULT_POLICY;
		}
		return new SimpleUpdatePolicy(updateTime);
	}
}
