/*
 * Copyright 2024 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel.uring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SubmissionQueueTest {

    @BeforeAll
    public static void loadJNI() {
        assumeTrue(IoUring.isAvailable());
    }

    @Test
    public void sqeFullTest() {
        RingBuffer ringBuffer = Native.createRingBuffer(8);
        try {
            SubmissionQueue submissionQueue = ringBuffer.ioUringSubmissionQueue();
            final CompletionQueue completionQueue = ringBuffer.ioUringCompletionQueue();

            assertNotNull(ringBuffer);
            assertNotNull(submissionQueue);
            assertNotNull(completionQueue);

            int counter = 0;
            while (submissionQueue.remaining() > 0) {
                assertThat(submissionQueue.addNop(0, 0, 0, (short) 1)).isNotZero();
                counter++;
            }
            assertEquals(8, counter);
            assertEquals(8, submissionQueue.count());
            assertThat(submissionQueue.addNop(0, 0, 0, (short) 1)).isNotZero();
            assertEquals(1, submissionQueue.count());
            submissionQueue.submitAndWait();
            assertEquals(9, completionQueue.count());
            assertEquals(9, completionQueue.getAsInt());
        } finally {
            ringBuffer.close();
        }
    }
}
