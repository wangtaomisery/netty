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
package io.netty.channel;

/**
 * {@link EventLoopGroup} for {@link IoEventLoop}s.
 */
public interface IoEventLoopGroup extends EventLoopGroup {

    @Override
    IoEventLoop next();

    /**
     * Returns {@code true} if the given type is compatible with this {@link IoEventLoopGroup} and so can be registered
     * to the contained {@link IoEventLoop}s, {@code false} otherwise.
     *
     * @param handleType    the type of the {@link IoHandle}.
     * @return              if compatible of not.
     */
    default boolean isCompatible(Class<? extends IoHandle> handleType) {
        return next().isCompatible(handleType);
    }

    /**
     * Returns {@code true} if the given {@link IoHandler} type is used by this {@link IoEventLoopGroup},
     * {@code false} otherwise.
     *
     * @param handlerType the type of the {@link IoHandler}.
     * @return            if used or not.
     */
    default boolean isIoType(Class<? extends IoHandler> handlerType) {
        return next().isIoType(handlerType);
    }
}
